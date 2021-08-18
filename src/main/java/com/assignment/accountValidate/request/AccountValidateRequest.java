package com.assignment.accountValidate.request;

import lombok.Data;

import java.util.Set;

@Data
public class AccountValidateRequest {

    String accountNumber;
    Set<String> providers;
}
