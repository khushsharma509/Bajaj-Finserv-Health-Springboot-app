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
    public void doOnStartup() {
        try {
            // === USER: UPDATE THIS INFO! ===
            String name = "John Doe";
            String regNo = "REG12347";
            String email = "john@example.com";
            // ================================

            // Prepare request to generate webhook
            Map<String, Object> reqBody = new HashMap<>();
            reqBody.put("name", name);
            reqBody.put("regNo", regNo);
            reqBody.put("email", email);

            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(reqBody, headers);

            ResponseEntity<WebhookResponse> resp =
                    restTemplate.postForEntity(url, entity, WebhookResponse.class);

            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                System.err.println("Failed to get webhook: " + resp.getStatusCode());
                return;
            }

            String webhookUrl = resp.getBody().getWebhook();
            String accessToken = resp.getBody().getAccessToken();

            // Choose SQL by registration number (last 2 digits even/odd)
            int lastTwo = Integer.parseInt(regNo.replaceAll("[^0-9]", "")
                                                .substring(regNo.replaceAll("[^0-9]", "").length() - 2));

            String yourSqlQuery;
            if (lastTwo % 2 == 0) {
                // Even = Question 2
                yourSqlQuery =
                        "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT "
                      + "FROM EMPLOYEE e1 "
                      + "JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID "
                      + "LEFT JOIN EMPLOYEE e2 "
                      + "ON e2.DEPARTMENT = e1.DEPARTMENT "
                      + "AND e2.DOB > e1.DOB "
                      + "GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME "
                      + "ORDER BY e1.EMP_ID DESC;";
                System.out.println("Question 2 (Even) assigned! See 'SQL-Qwestion-2-JAVA.pdf'.");
            } else {
                // Odd = Question 1
                yourSqlQuery =
                        "SELECT p.AMOUNT AS SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, "
                      + "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, d.DEPARTMENT_NAME "
                      + "FROM PAYMENTS p "
                      + "JOIN EMPLOYEE e ON e.EMP_ID = p.EMP_ID "
                      + "JOIN DEPARTMENT d ON d.DEPARTMENT_ID = e.DEPARTMENT "
                      + "WHERE DAY(p.PAYMENT_TIME) <> 1 "
                      + "ORDER BY p.AMOUNT DESC "
                      + "LIMIT 1;";
                System.out.println("Question 1 (Odd) assigned! See 'SQL-Question-1-JAVA.pdf'.");
            }

            // Prepare JSON with the final SQL for submission
            HttpHeaders subHeaders = new HttpHeaders();
            subHeaders.setContentType(MediaType.APPLICATION_JSON);
            subHeaders.setBearerAuth(accessToken);

            Map<String, String> solBody = Map.of("finalQuery", yourSqlQuery);
            HttpEntity<Map<String, String>> subReq = new HttpEntity<>(solBody, subHeaders);

            ResponseEntity<String> submitResp = restTemplate.postForEntity(webhookUrl, subReq, String.class);

            System.out.println("Submission response: " + submitResp.getStatusCode());
            System.out.println("Response body: " + submitResp.getBody());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
