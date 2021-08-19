package com.assignment.accountvalidator.service;

import com.assignment.accountvalidator.exceptions.AccountValidateException;
import com.assignment.accountvalidator.request.AccountValidateRequest;
import com.assignment.accountvalidator.response.AccountValidateResponse;
import com.assignment.accountvalidator.utils.DataProviderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountValidateService {

    @Autowired
    DataProviderUtils dataProviderUtils;

    public AccountValidateResponse validateAccount(AccountValidateRequest accountValidateRequest) throws AccountValidateException {

        log.info("Fetching results of the account number: " + accountValidateRequest.getAccountNumber());
        return dataProviderUtils.validateAccountWithProviders(accountValidateRequest);

    }
}
