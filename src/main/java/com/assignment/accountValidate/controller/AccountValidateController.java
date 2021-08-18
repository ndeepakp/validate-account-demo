package com.assignment.accountValidate.controller;

import com.assignment.accountValidate.exceptions.AccountValidateException;
import com.assignment.accountValidate.request.AccountValidateRequest;
import com.assignment.accountValidate.response.AccountValidateResponse;
import com.assignment.accountValidate.service.AccountValidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountValidateController {

    @Autowired
    AccountValidateService accountValidateService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountValidateController.class);

    @RequestMapping(value="/validate-account", method = RequestMethod.OPTIONS)
    ResponseEntity<?> optionsForValidateAccount()
    {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.POST, HttpMethod.OPTIONS)
                .build();
    }

    @PostMapping(produces = "application/json", value = "/validate-account")
    public ResponseEntity<String> validateAccountWithProviders(@RequestBody AccountValidateRequest accountValidateRequest) {

        AccountValidateResponse response;

        try {
            response = accountValidateService.validateAccount(accountValidateRequest);
        } catch (AccountValidateException e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        LOGGER.info("Output as requested: " + response);

        return new ResponseEntity(response, HttpStatus.OK);
    }
}
