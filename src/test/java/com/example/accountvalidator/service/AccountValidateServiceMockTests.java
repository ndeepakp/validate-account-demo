package com.example.accountvalidator.service;

import com.assignment.accountvalidator.exceptions.AccountValidateException;
import com.assignment.accountvalidator.request.AccountValidateRequest;
import com.assignment.accountvalidator.response.AccountValidateResponse;
import com.assignment.accountvalidator.service.AccountValidateService;
import com.assignment.accountvalidator.utils.DataProviderUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountValidateService.class)
class AccountValidateServiceMockTests {

    @Autowired
    AccountValidateService accountValidateService;

    @MockBean
    DataProviderUtils dataProviderUtils;

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
    @DisplayName("Verify if the application is able to load the Spring context.")
    public void contextLoads() {
        // Just to check if spring application context gets loaded or not.
    }

    @Test
    @DisplayName("Verify if data provider util was invoked.")
    public void verifyServiceCallsDataProvider() {

        doReturn(new AccountValidateResponse()).when(dataProviderUtils).validateAccountWithProviders(Mockito.any());
        Object response =
                accountValidateService.validateAccount(accountValidateRequest);

        Mockito.verify(dataProviderUtils, times(1)).validateAccountWithProviders(Mockito.any());

        Mockito.verifyNoMoreInteractions(dataProviderUtils);
        Assert.assertTrue(response != null);

    }

    @Test
    @DisplayName("Verify if data provider is getting the input that is sent to service class.")
    public void verifyInputFromServiceToDataProvider() {

        accountValidateService.validateAccount(accountValidateRequest);
        ArgumentCaptor<AccountValidateRequest> requestCaptor = ArgumentCaptor.forClass(AccountValidateRequest.class);

        Mockito.verify(dataProviderUtils, times(1)).validateAccountWithProviders(requestCaptor.capture());
        AccountValidateRequest request = requestCaptor.getValue();

        Assert.assertTrue(request.getAccountNumber().equals(accountValidateRequest.getAccountNumber()));
        Assert.assertTrue(request.getProviders().size() == (accountValidateRequest.getProviders().size()));
    }


}
