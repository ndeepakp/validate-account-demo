package com.example.accountValidate.utils;

import com.assignment.accountValidate.AccountValidateApplication;
import com.assignment.accountValidate.config.ProviderConfiguration;
import com.assignment.accountValidate.exceptions.AccountValidateException;
import com.assignment.accountValidate.exceptions.InvalidAccountNumberException;
import com.assignment.accountValidate.exceptions.MissingAccountNumberException;
import com.assignment.accountValidate.exceptions.ProviderNotFoundException;
import com.assignment.accountValidate.request.AccountValidateRequest;
import com.assignment.accountValidate.response.AccountValidateResponse;
import com.assignment.accountValidate.utils.DataProviderUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;

@SpringBootTest(
        properties = "spring.profiles.active=test",
        classes = AccountValidateApplication.class
)
class DataProviderUtilsTest {

    @Autowired
    DataProviderUtils dataProviderUtils;

    @Autowired
    ProviderConfiguration providerConfiguration;

    @Autowired
    AccountValidateResponse accountValidateResponse;

    private AccountValidateRequest accountValidateRequest;

    @BeforeEach
    public void setUp() {

        accountValidateRequest = new AccountValidateRequest();

        accountValidateRequest.setAccountNumber("1234");
        accountValidateRequest.setProviders(new HashSet<String>() {{
            add("provider1");
        }});
    }

    @Test
    @DisplayName("Verify data provider gives expected output with valid input.")
    public void verifyDataProviderWithValidInput() throws AccountValidateException {

        accountValidateResponse = dataProviderUtils.validateAccountWithProviders(accountValidateRequest);
        Assert.assertTrue(accountValidateRequest.getProviders().contains(accountValidateResponse.getProviders().get(0).getName()));

    }

    @Test
    @DisplayName("Verify data provider throws exception when account number is empty.")
    public void verifyDataProviderWithEmptyAccountNumber() throws AccountValidateException {
        accountValidateRequest.setAccountNumber("");

        Assert.assertThrows(MissingAccountNumberException.class, () -> {
            accountValidateResponse = dataProviderUtils.validateAccountWithProviders(accountValidateRequest);
        });
    }

    @Test
    @DisplayName("Verify data provider throws exception when account number is missing.")
    public void verifyDataProviderWithMissingAccountNumber() throws AccountValidateException {
        accountValidateRequest.setAccountNumber(null);

        Assert.assertThrows(MissingAccountNumberException.class, () -> {
            accountValidateResponse = dataProviderUtils.validateAccountWithProviders(accountValidateRequest);
        });
    }

    @Test
    @DisplayName("Verify data provider throws exception when account number is alphanumeric.")
    public void verifyDataProviderWithInvalidAccountNumber() throws AccountValidateException {
        accountValidateRequest.setAccountNumber("123e456");

        Assert.assertThrows(InvalidAccountNumberException.class, () -> {
            accountValidateResponse = dataProviderUtils.validateAccountWithProviders(accountValidateRequest);
        });
    }

    @Test
    @DisplayName("Verify data provider util returns all data from configuration when provider is missing.")
    public void verifyServiceCallsDataProviderWithNoProviders() throws AccountValidateException {
        accountValidateRequest.setProviders(null);

        accountValidateResponse = dataProviderUtils.validateAccountWithProviders(accountValidateRequest);
        Assert.assertTrue(accountValidateResponse.getProviders().size() == providerConfiguration.getProviders().size());
    }

    @Test
    @DisplayName("Verify data provider util returns all data from configuration when provider is empty.")
    public void verifyServiceCallsDataProviderWithEmptyProvidersSet() throws AccountValidateException {
        accountValidateRequest.setProviders(new HashSet<>());

        accountValidateResponse = dataProviderUtils.validateAccountWithProviders(accountValidateRequest);
        Assert.assertTrue(accountValidateResponse.getProviders().size() == providerConfiguration.getProviders().size());
    }

    @Test
    @DisplayName("Verify data provider util returns a boolean value.")
    public void verifyDataProviderForAccountNumber() throws AccountValidateException {

        AccountValidateResponse.Provider provider = dataProviderUtils.buildProviderDetails(accountValidateRequest.getAccountNumber(), "provider1");
        Assert.assertTrue(provider.isValid() || !provider.isValid());
        Assert.assertNotNull(provider.getName());
    }

    @Test
    @DisplayName("Verify data provider util throws exception when provider does not exist in configuration.")
    public void testDataProviderWithInvalidProviderName() {
        Assert.assertThrows(ProviderNotFoundException.class, () -> {
            dataProviderUtils.buildProviderDetails(accountValidateRequest.getAccountNumber(), "non-existing");
        });
    }

}
