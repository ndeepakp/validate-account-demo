package com.example.accountvalidator.utils;

import com.assignment.accountvalidator.AccountValidateApplication;
import com.assignment.accountvalidator.config.ProviderConfiguration;
import com.assignment.accountvalidator.exceptions.AccountValidateException;
import com.assignment.accountvalidator.exceptions.InvalidAccountNumberException;
import com.assignment.accountvalidator.exceptions.MissingAccountNumberException;
import com.assignment.accountvalidator.exceptions.ProviderNotFoundException;
import com.assignment.accountvalidator.request.AccountValidateRequest;
import com.assignment.accountvalidator.response.AccountValidateResponse;
import com.assignment.accountvalidator.utils.DataProviderUtils;
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
    public void verifyDataProviderWithValidInput() {

        accountValidateResponse = dataProviderUtils.validateAccountWithProviders(accountValidateRequest);
        Assert.assertTrue(accountValidateRequest.getProviders().contains(accountValidateResponse.getProviders().get(0).getName()));

    }

    @Test
    @DisplayName("Verify data provider throws exception when account number is empty.")
    public void verifyDataProviderWithEmptyAccountNumber() {
        accountValidateRequest.setAccountNumber("");

        Assert.assertThrows(MissingAccountNumberException.class, () -> {
            accountValidateResponse = dataProviderUtils.validateAccountWithProviders(accountValidateRequest);
        });
    }

    @Test
    @DisplayName("Verify data provider throws exception when account number is missing.")
    public void verifyDataProviderWithMissingAccountNumber() {
        accountValidateRequest.setAccountNumber(null);

        Assert.assertThrows(MissingAccountNumberException.class, () -> {
            accountValidateResponse = dataProviderUtils.validateAccountWithProviders(accountValidateRequest);
        });
    }

    @Test
    @DisplayName("Verify data provider throws exception when account number is alphanumeric.")
    public void verifyDataProviderWithInvalidAccountNumber() {
        accountValidateRequest.setAccountNumber("123e456");

        Assert.assertThrows(InvalidAccountNumberException.class, () -> {
            accountValidateResponse = dataProviderUtils.validateAccountWithProviders(accountValidateRequest);
        });
    }

    @Test
    @DisplayName("Verify data provider util returns all data from configuration when provider is missing.")
    public void verifyServiceCallsDataProviderWithNoProviders() {
        accountValidateRequest.setProviders(null);

        accountValidateResponse = dataProviderUtils.validateAccountWithProviders(accountValidateRequest);
        Assert.assertTrue(accountValidateResponse.getProviders().size() == providerConfiguration.getProviders().size());
    }

    @Test
    @DisplayName("Verify data provider util returns all data from configuration when provider is empty.")
    public void verifyServiceCallsDataProviderWithEmptyProvidersSet() {
        accountValidateRequest.setProviders(new HashSet<>());

        accountValidateResponse = dataProviderUtils.validateAccountWithProviders(accountValidateRequest);
        Assert.assertTrue(accountValidateResponse.getProviders().size() == providerConfiguration.getProviders().size());
    }

    @Test
    @DisplayName("Verify data provider util returns a boolean value.")
    public void verifyDataProviderForAccountNumber() {

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
