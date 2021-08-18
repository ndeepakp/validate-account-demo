package com.assignment.accountValidate.service;

import com.assignment.accountValidate.exceptions.AccountValidateException;
import com.assignment.accountValidate.request.AccountValidateRequest;
import com.assignment.accountValidate.response.AccountValidateResponse;
import com.assignment.accountValidate.utils.DataProviderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountValidateService {

    @Autowired
    DataProviderUtils dataProviderUtils;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountValidateService.class);

    public AccountValidateResponse validateAccount(AccountValidateRequest accountValidateRequest) throws AccountValidateException {

        LOGGER.info("Fetching results of the account number: " + accountValidateRequest.getAccountNumber());
        return dataProviderUtils.validateAccountWithProviders(accountValidateRequest);

    }
}
