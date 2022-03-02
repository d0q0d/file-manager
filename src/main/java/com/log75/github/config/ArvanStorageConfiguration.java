package com.log75.github.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("arvan")
@Component
@Data
public class ArvanStorageConfiguration {
    private String accessKey;
    private String secretKey;
    private String endpoint;
}
