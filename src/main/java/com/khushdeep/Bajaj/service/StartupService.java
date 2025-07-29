package com.khushdeep.Bajaj.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.khushdeep.Bajaj.model.WebhookResponse;

import jakarta.annotation.PostConstruct;

@Service
public class StartupService {
    @PostConstruct
    public void startup() {
        try {
            
            String name = "Khushdeep Sharma";
            String regNo = "2210990509";
            String email = "khushdeep509.be22@chitkara.edu.in";

            // 1. Generate webhook and access token
            RestTemplate restTemplate = new RestTemplate();
            String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("name", name);
            requestBody.put("regNo", regNo);
            requestBody.put("email", email);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(generateUrl, entity, WebhookResponse.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                System.err.println("Failed to generate webhook: " + response.getStatusCode());
                return;
            }

            String webhookUrl = response.getBody().getWebhook();
            String accessToken = response.getBody().getAccessToken();

            // 2. Decide SQL based on regNo (odd = Q1, even = Q2)
            int lastTwo = Integer.parseInt(regNo.replaceAll("[^0-9]", "").substring(regNo.length() - 2));
            String sqlQuery = (
                lastTwo % 2 == 0
                  ? // EVEN --> Q2 (provided for reference, not needed for you currently)
                    "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT " +
                    "FROM EMPLOYEE e1 " +
                    "JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID " +
                    "LEFT JOIN EMPLOYEE e2 ON e2.DEPARTMENT = e1.DEPARTMENT AND e2.DOB > e1.DOB " +
                    "GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME " +
                    "ORDER BY e1.EMP_ID DESC;"
                  : // ODD --> Q1
                    "SELECT p.AMOUNT AS SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
                    "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, d.DEPARTMENT_NAME " +
                    "FROM PAYMENTS p " +
                    "JOIN EMPLOYEE e ON e.EMP_ID = p.EMP_ID " +
                    "JOIN DEPARTMENT d ON d.DEPARTMENT_ID = e.DEPARTMENT " +
                    "WHERE DAY(p.PAYMENT_TIME) <> 1 " +
                    "ORDER BY p.AMOUNT DESC LIMIT 1;"
            );

            // 3. Send answer to the webhook, JWT as Bearer!
            HttpHeaders answerHeaders = new HttpHeaders();
            answerHeaders.setContentType(MediaType.APPLICATION_JSON);
            answerHeaders.setBearerAuth(accessToken);

            Map<String, String> answerBody = Map.of("finalQuery", sqlQuery);

            HttpEntity<Map<String, String>> answerEntity = new HttpEntity<>(answerBody, answerHeaders);

            ResponseEntity<String> submitResponse = restTemplate.postForEntity(webhookUrl, answerEntity, String.class);

            System.out.println("Submission Response: " + submitResponse.getStatusCode());
            System.out.println("Body: " + submitResponse.getBody());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}