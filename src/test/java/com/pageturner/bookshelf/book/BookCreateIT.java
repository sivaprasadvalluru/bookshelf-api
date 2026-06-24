package com.pageturner.bookshelf.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BookCreateIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createBook_returns201WithId() {
        Map<String, String> body = Map.of(
                "title", "The Left Hand of Darkness",
                "author", "Ursula K. Le Guin",
                "isbn", "978-0-441-47812-5"
        );

        ResponseEntity<Map> response = restTemplate.postForEntity("/api/books", body, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("id")).isNotNull();
        assertThat(response.getBody().get("title")).isEqualTo("The Left Hand of Darkness");
    }

    @Test
    void createBook_duplicateIsbn_returns409() {
        // Dune is already in the catalog via seed data
        Map<String, String> body = Map.of(
                "title", "Dune Duplicate",
                "author", "Frank Herbert",
                "isbn", "978-0-441-17271-9"
        );

        ResponseEntity<Map> response = restTemplate.postForEntity("/api/books", body, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
