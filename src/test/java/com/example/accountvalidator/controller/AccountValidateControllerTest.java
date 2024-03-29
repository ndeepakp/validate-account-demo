package com.example.accountvalidator.controller;

import com.assignment.accountvalidator.AccountValidateApplication;
import com.assignment.accountvalidator.config.ProviderConfiguration;
import com.assignment.accountvalidator.request.AccountValidateRequest;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest(
        properties = "spring.profiles.active=test",
        classes = AccountValidateApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class AccountValidateControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProviderConfiguration providerConfiguration;

    private AccountValidateRequest accountValidateRequest;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @BeforeEach
    public void setUp() {

        accountValidateRequest = new AccountValidateRequest();

        accountValidateRequest.setAccountNumber("1234");
        accountValidateRequest.setProviders(new HashSet<String>() {{
            add("provider1");
        }});
    }

    @Test
    @DisplayName("Positive test for OPTIONS /account/validate api to check supported HTTP Methods")
    public void testOptionsForValidateAccountController() {
        Set<HttpMethod> supportedMethods = restTemplate.optionsForAllow(getRootUrl() + "/account/validate");
        Assert.assertNotNull(supportedMethods);
        Assert.assertFalse(supportedMethods.isEmpty());
        Assert.assertTrue(supportedMethods.contains(HttpMethod.OPTIONS));
        Assert.assertTrue(supportedMethods.contains(HttpMethod.POST));
    }

    @Test
    @DisplayName("Positive test for validate-account api with valid inputs")
    public void testValidateAccountController() {
        ResponseEntity<String> postResponse = restTemplate.postForEntity(getRootUrl() + "/account/validate",
                accountValidateRequest, String.class);
        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
        Assert.assertEquals(postResponse.getStatusCodeValue(), 200);
    }

    @Test
    @DisplayName("Test validate-account api with empty accountNumber")
    public void testValidateAccountControllerWithEmptyAccountNumber() {
        accountValidateRequest.setAccountNumber("");
        ResponseEntity<String> postResponse = restTemplate.postForEntity(getRootUrl() + "/account/validate",
                accountValidateRequest, String.class);
        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
        Assert.assertTrue(postResponse.getBody().contains("Account Number is missing or"));
        Assert.assertEquals(postResponse.getStatusCodeValue(), 400);
    }

    @Test
    @DisplayName("Test validate-account api with no accountNumber")
    public void testValidateAccountControllerWithMissingAccountNumber() {
        accountValidateRequest.setAccountNumber(null);
        ResponseEntity<String> postResponse = restTemplate.postForEntity(getRootUrl() + "/account/validate",
                accountValidateRequest, String.class);
        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
        Assert.assertTrue(postResponse.getBody().contains("Account Number is missing or"));
        Assert.assertEquals(postResponse.getStatusCodeValue(), 400);
    }

    @Test
    @DisplayName("Test validate-account api with alphanumeric accountNumber")
    public void testValidateAccountControllerWithAlphaNumericAccountNumber() {
        accountValidateRequest.setAccountNumber("123e456");
        ResponseEntity<String> postResponse = restTemplate.postForEntity(getRootUrl() + "/account/validate",
                accountValidateRequest, String.class);
        System.out.println(postResponse.getBody());
        Assert.assertNotNull(postResponse);
        Assert.assertTrue(postResponse.getBody().contains("Account Number is not valid"));
        Assert.assertEquals(postResponse.getStatusCodeValue(), 400);
    }

    @Test
    @DisplayName("Test validate-account api with empty provider list")
    public void testValidateAccountControllerWithNoProvider() {
        accountValidateRequest.setProviders(new HashSet<>());
        ResponseEntity<String> postResponse = restTemplate.postForEntity(getRootUrl() + "/account/validate",
                accountValidateRequest, String.class);
        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
        Assert.assertEquals(postResponse.getStatusCodeValue(), 200);
    }

    @Test
    @DisplayName("Test validate-account api with duplicate providers")
    public void testValidateAccountControllerWithDuplicateProvider() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("{\n" +
                " \"accountNumber\": \"1245678965321\",\n" +
                " \"providers\": [\n" +
                " \"provider1\",\n" +
                " \"provider2\",\n" +
                " \"provider2\"\n" +
                " ]\n" +
                "}", headers);

        ResponseEntity<String> postResponse = restTemplate.postForEntity(getRootUrl() + "/account/validate",
                entity, String.class);

        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
        Assert.assertEquals(postResponse.getStatusCodeValue(), 200);
    }

    @Test
    @DisplayName("Test validate-account api with no providers")
    public void testValidateAccountControllerWithMissingProviderParam() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("{\n" +
                " \"accountNumber\": \"1245678965321\"}", headers);

        ResponseEntity<String> postResponse = restTemplate.postForEntity(getRootUrl() + "/account/validate",
                entity, String.class);

        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
        Assert.assertEquals(postResponse.getStatusCodeValue(), 200);
    }

    @Test
    @DisplayName("Test validate-account api with content type other thank APPLICATION_JSON media type")
    public void testValidateAccountControllerWithInvalidContentType() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> entity = new HttpEntity<>("{\n" +
                " \"accountNumber\": \"1245678965321\"}", headers);

        ResponseEntity<String> postResponse = restTemplate.postForEntity(getRootUrl() + "/account/validate",
                entity, String.class);

        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
        Assert.assertEquals(postResponse.getStatusCodeValue(), 415);
    }

    @Test
    @DisplayName("Test validate-account api with invalid provider")
    public void testValidateAccountControllerWithInvalidProvider() {
        accountValidateRequest.getProviders().add("provider-not-in-use");
        ResponseEntity<String> postResponse = restTemplate.postForEntity(getRootUrl() + "/account/validate",
                accountValidateRequest, String.class);
        System.out.println(postResponse.getBody());
        Assert.assertNotNull(postResponse);
        Assert.assertTrue(postResponse.getBody().contains("Provider Name not found: <"));
        Assert.assertEquals(postResponse.getStatusCodeValue(), 400);
    }

}
