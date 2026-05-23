import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthIntegrationTest {

    private static RestClient restClient;

    @BeforeAll
    static void setup() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:4004")
                .build();
    }

    @Test
    public void shouldReturnOkWithValidToken() {
        String loginPayload = """
                {
                    "email" : "testuser@test.com",
                    "password" : "password123"
                }
                """;

        // Execute POST request and extract the response body as a Map
        Map response = restClient
                .post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginPayload)
                .retrieve()
                .body(Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("token")).isNotNull();

        System.out.println("Generated Token: " + response.get("token"));
    }

    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin() {
        String loginPayload = """
                {
                    "email" : "invalid_testuser@test.com",
                    "password" : "wrong_password"
                }
                """;

        // Spring RestClient throws HttpClientErrorException for 4xx HTTP statuses
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restClient
                    .post()
                    .uri("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(loginPayload)
                    .retrieve()
                    .toBodilessEntity();
        });

        assertThat(exception.getStatusCode().value()).isEqualTo(401);
    }
}