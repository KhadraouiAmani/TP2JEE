package com.bibliotech.bibliotechproject;

import com.bibliotech.bibliotechproject.dtos.LoginRequest;
import com.bibliotech.bibliotechproject.models.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

// This starts the whole app on a random port for the test
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testLoginWithCorrectCredentials() {
        LoginRequest login = new LoginRequest();
        login.setUsername("admin");
        login.setPassword("admin123");

        webTestClient.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(login)
                .exchange()
                .expectStatus().isOk() // Check if 200 OK
                .expectBody()
                .jsonPath("$.token").exists(); // Check if a token is returned
    }

    @Test
    void testCreateBookWithoutTokenShouldReturn401() {
        // We try to add a book without the "Bearer" token
        Book book = new Book();
        book.setIsbn("TEST-123");
        book.setTitle("Unauthorized Book");

        webTestClient.post().uri("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(book)
                .exchange()
                .expectStatus().isUnauthorized(); // Should be 401
    }

    @Test
    void testCreateBookWithAdminAccess() {
        // 1. First we login to get a real token
        LoginRequest login = new LoginRequest();
        login.setUsername("admin");
        login.setPassword("admin123");

        String token = webTestClient.post().uri("/api/v1/auth/login")
                .bodyValue(login)
                .exchange()
                .expectBody(com.bibliotech.bibliotechproject.dtos.AuthResponse.class)
                .returnResult().getResponseBody().getToken();

        // 2. We use that token to add a book
        Book book = new Book();
        book.setIsbn("ISBN-AUTO-TEST");
        book.setTitle("Automated Test Book");
        book.setStockDisponible(5);

        webTestClient.post().uri("/api/v1/books")
                .header("Authorization", "Bearer " + token) // We swipe the "Key Card"
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(book)
                .exchange()
                .expectStatus().isCreated() // Check for 201 Created
                .expectBody()
                .jsonPath("$.isbn").isEqualTo("ISBN-AUTO-TEST");
    }
}