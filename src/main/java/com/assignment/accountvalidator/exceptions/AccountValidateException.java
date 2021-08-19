package com.assignment.accountvalidator.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountValidateException extends Exception {

    public AccountValidateException (String message) {
        super(message);
        log.error(message);
    }

}