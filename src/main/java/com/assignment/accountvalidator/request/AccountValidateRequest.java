package com.assignment.accountvalidator.request;

import lombok.Data;

import java.util.Set;

@Data
public class AccountValidateRequest {

    String accountNumber;
    Set<String> providers;
}
