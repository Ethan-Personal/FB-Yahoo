package com.example.fbyahoo.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "yahoo")
public class YahooProperties {

    private OAuth oauth;
    private Api api;

    @Getter
    @Setter
    public static class OAuth {
        private String tokenUrl;
        private String authorizeUrl;
        private String redirectUri;
        private String clientId;
        private String clientSecret;
        private String baseUrl;
    }

    @Getter
    @Setter
    public static class Api {
        private String baseUrl;
    }
}
