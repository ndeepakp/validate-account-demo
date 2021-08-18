package com.assignment.accountValidate.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties
@Data
public class ProviderConfiguration {

    protected List<Map<String, String>> providers = new ArrayList<>();
}
