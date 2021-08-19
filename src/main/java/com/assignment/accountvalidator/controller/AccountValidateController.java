package com.assignment.accountvalidator.controller;

import com.assignment.accountvalidator.exceptions.AccountValidateException;
import com.assignment.accountvalidator.request.AccountValidateRequest;
import com.assignment.accountvalidator.response.AccountValidateResponse;
import com.assignment.accountvalidator.service.AccountValidateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class AccountValidateController {

    @Autowired
    AccountValidateService accountValidateService;

    @RequestMapping(value="/account/validate", method = RequestMethod.OPTIONS)
    ResponseEntity<?> optionsForValidateAccount()
    {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.POST, HttpMethod.OPTIONS)
                .build();
    }

    @PostMapping(produces = "application/json", value = "/account/validate")
    public ResponseEntity<String> validateAccountWithProviders(@RequestBody AccountValidateRequest accountValidateRequest) {

        AccountValidateResponse response;

        try {
            response = accountValidateService.validateAccount(accountValidateRequest);
        } catch (AccountValidateException e) {
            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        log.info("Output as requested: " + response);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
