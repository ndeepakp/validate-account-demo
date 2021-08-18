package com.assignment.accountValidate.response;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class AccountValidateResponse {

    @Data
    public class Provider {

        String name;
        boolean isValid;

        @Override
        public String toString() {

            return "{" +
                    "\"provider\": \"" + getName() + "\"," +
                    "\"isValid\": " + isValid() +
                    "}";
        }
    }

    List<Provider> providers;

    @Override
    public String toString() {

        return "{\"result\": " + getProviders() + "}";
    }
}
