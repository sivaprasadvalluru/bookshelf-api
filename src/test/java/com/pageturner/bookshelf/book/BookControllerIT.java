package com.pageturner.bookshelf.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BookControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @SuppressWarnings("unchecked")
    void listBooks_containsSeedTitles() {
        ResponseEntity<List> response = restTemplate.getForEntity("/api/books", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Map<String, Object>> books = response.getBody();
        assertThat(books).isNotNull();

        List<String> titles = books.stream()
                .map(b -> (String) b.get("title"))
                .toList();

        assertThat(titles).contains("Dune", "Foundation", "Neuromancer");
    }
}
