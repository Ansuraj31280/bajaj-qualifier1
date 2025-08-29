package com.ansu.bajaj;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class BajajApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BajajApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "Ansu Raj");     
        requestBody.put("regNo", "22BEC0186");       
        requestBody.put("email", "ansu.raj2022@vitstudent.ac.in"); 

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        System.out.println("Response from GenerateWebhook API: " + response.getBody());
        String webhookUrl = (String) response.getBody().get("webhook");
        String accessToken = (String) response.getBody().get("accessToken");
        String finalQuery = "SELECT e1.EMP_ID, " +
                "e1.FIRST_NAME, " +
                "e1.LAST_NAME, " +
                "d.DEPARTMENT_NAME, " +
                "(SELECT COUNT(*) FROM EMPLOYEE e2 " +
                " WHERE e2.DEPARTMENT = e1.DEPARTMENT " +
                " AND e2.DOB > e1.DOB) AS YOUNGER_EMPLOYEES_COUNT " +
                "FROM EMPLOYEE e1 " +
                "JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID " +
                "ORDER BY e1.EMP_ID DESC;";

        Map<String, String> solutionBody = new HashMap<>();
        solutionBody.put("finalQuery", finalQuery);

        HttpHeaders solutionHeaders = new HttpHeaders();
        solutionHeaders.setContentType(MediaType.APPLICATION_JSON);
        solutionHeaders.set("Authorization", accessToken);

        HttpEntity<Map<String, String>> solutionEntity = new HttpEntity<>(solutionBody, solutionHeaders);

        ResponseEntity<String> submitResponse =
                restTemplate.exchange(webhookUrl, HttpMethod.POST, solutionEntity, String.class);

        System.out.println("Submission Response: " + submitResponse.getBody());
    }
}
