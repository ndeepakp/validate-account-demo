package com.assignment.accountValidate.utils;

import com.assignment.accountValidate.config.ProviderConfiguration;
import com.assignment.accountValidate.exceptions.AccountValidateException;
import com.assignment.accountValidate.exceptions.InvalidAccountNumberException;
import com.assignment.accountValidate.exceptions.MissingAccountNumberException;
import com.assignment.accountValidate.exceptions.ProviderNotFoundException;
import com.assignment.accountValidate.request.AccountValidateRequest;
import com.assignment.accountValidate.response.AccountValidateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Component
public class DataProviderUtils {

    @Autowired
    ProviderConfiguration providerConfiguration;

    @Autowired
    AccountValidateResponse accountValidateResponse;

    @Bean
    public Random getRandom() {
        return new Random();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DataProviderUtils.class);

    protected boolean isNumeric(String accountNumber) throws AccountValidateException {

        if (String.valueOf(accountNumber).equals("null") || accountNumber.isEmpty()) {
            throw new MissingAccountNumberException("Account Number is missing or is empty.");
        }

        if (!accountNumber.matches("\\d+(\\d+)?")) {
            throw new InvalidAccountNumberException("Account Number is not valid.");
        }

        LOGGER.info("Account Number is Valid.");
        return true;
    }

    public AccountValidateResponse validateAccountWithProviders(AccountValidateRequest accountValidateRequest) throws AccountValidateException {

        String accountNumber = accountValidateRequest.getAccountNumber();
        Set<String> providers = accountValidateRequest.getProviders();

        LOGGER.info("Validating details of the account number against the providers: " + providers);

        if (accountValidateResponse.getProviders() != null) {
            accountValidateResponse.getProviders().clear();
        } else {
            accountValidateResponse.setProviders(new ArrayList<>());
        }


        if (isNumeric(accountNumber) && providers != null && !providers.isEmpty()) {

            for (String p : providers) {
                accountValidateResponse.getProviders().add(buildProviderDetails(accountNumber, p));
            }

            return accountValidateResponse;
        } else {

            return buildAllProviderDetails(accountNumber);
        }
    }

    public boolean validateAccountWithProvider(String accountNumber, String url) {

        Boolean b = getRandom().nextBoolean();
        LOGGER.info("Hitting url: " + b);
        return Long.parseLong(accountNumber) % 2 == 0 || getRandom().nextBoolean();
    }

    public AccountValidateResponse.Provider buildProviderDetails(String accountNumber, String providerName) throws ProviderNotFoundException {

        System.out.println(providerConfiguration.getProviders());
        for (Map<String, String> map : providerConfiguration.getProviders()) {
            if (map.get("name").toLowerCase().trim().equals(providerName.toLowerCase().trim())) {

                AccountValidateResponse.Provider provider = accountValidateResponse.new Provider();

                provider.setName(map.get("name"));
                provider.setValid(validateAccountWithProvider(accountNumber, map.get("url")));
                return provider;
            }
        }

        throw new ProviderNotFoundException("Provider Name not found: <" + providerName + ">.");
    }

    protected AccountValidateResponse buildAllProviderDetails(String accountNumber) throws ProviderNotFoundException {


        LOGGER.info("Validating details of the account number against all the providers.");

        for (Map<String, String> map : providerConfiguration.getProviders()) {

            accountValidateResponse.getProviders().add(buildProviderDetails(accountNumber, map.get("name")));

        }

        return accountValidateResponse;
    }
}
