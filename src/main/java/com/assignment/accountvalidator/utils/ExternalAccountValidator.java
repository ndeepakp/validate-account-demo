package com.assignment.accountvalidator.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class ExternalAccountValidator {

    @Bean
    public Random getRandom() {
        return new Random();
    }

    public boolean validateAccountWithProvider(String accountNumber, String url) {

        log.info("Hitting url: " + url);
        return Long.parseLong(accountNumber) % 2 == 0 || getRandom().nextBoolean();
    }

}
