package com.assignment.accountValidate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingAccountNumberException extends AccountValidateException {

    public MissingAccountNumberException(String message) {
        super(message);
    }

}
