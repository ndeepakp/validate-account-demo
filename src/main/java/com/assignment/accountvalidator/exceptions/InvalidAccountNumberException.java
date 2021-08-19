package com.assignment.accountvalidator.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Slf4j
public class InvalidAccountNumberException extends AccountValidateException {

    public InvalidAccountNumberException(String message) {
        super(message);
        log.error(message);
    }

}
