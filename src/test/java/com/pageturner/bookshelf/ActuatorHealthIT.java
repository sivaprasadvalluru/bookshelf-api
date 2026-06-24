package com.pageturner.bookshelf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ActuatorHealthIT {

    @LocalManagementPort
    private int managementPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void healthEndpointReturnsUp() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + managementPort + "/actuator/health",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"status\":\"UP\"");
    }

    @Test
    void healthEndpointIncludesDatabaseComponent() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + managementPort + "/actuator/health",
                String.class);

        assertThat(response.getBody()).contains("\"db\"");
        assertThat(response.getBody()).contains("\"status\":\"UP\"");
    }
}
