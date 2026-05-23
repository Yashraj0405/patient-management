import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PatientIntegrationTest {

    private static RestClient restClient;

    @BeforeAll
    static void setup() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:4004")
                .build();
    }

    @Test
    public void shouldReturnPatientsWithValidToken() {
        String loginPayload = """
                {
                    "email" : "testuser@test.com",
                    "password" : "password123"
                }
                """;

        // 1. Authenticate and extract token (This endpoint returns an object {}, so Map works)
        @SuppressWarnings("unchecked")
        Map<String, Object> authResponse = restClient
                .post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginPayload)
                .retrieve()
                .body(Map.class);

        assertThat(authResponse).isNotNull();
        String token = (String) authResponse.get("token");
        assertThat(token).isNotNull();

        // 2. Query secure endpoint using Bearer Token
        // FIXED: Using ParameterizedTypeReference to parse a root-level JSON Array [...] into a List
        List<Map<String, Object>> patientsList = restClient
                .get()
                .uri("/api/patients")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        // 3. Verify data integrity on the collection
        assertThat(patientsList).isNotNull();
        assertThat(patientsList).isNotEmpty(); // Asserts that the array has at least one patient record

        // Let's print out the first patient's details in the console to verify fields
        System.out.println("First Patient Data: " + patientsList.getFirst());
    }
}