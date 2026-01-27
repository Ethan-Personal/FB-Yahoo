package com.example.fbyahoo.service;

import com.example.fbyahoo.config.YahooProperties;
import com.example.fbyahoo.dto.YahooTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Base64;

@Service
public class YahooOAuthService {

    private final YahooProperties yahooProperties;
    private final WebClient oauthClient;


    public YahooOAuthService(YahooProperties yahooProperties, WebClient.Builder builder) {
        this.yahooProperties = yahooProperties;
        this.oauthClient = builder
                .baseUrl(yahooProperties.getOauth().getBaseUrl())
                .defaultHeader(MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


    public String buildAuthorizeUrl(String state) {

        String authorizeUrl = yahooProperties.getOauth().getBaseUrl() +
                            yahooProperties.getOauth().getAuthorizeUrl();

        return UriComponentsBuilder
                .fromUriString(authorizeUrl)
                .queryParam("client_id", yahooProperties.getOauth().getClientId())
                .queryParam("redirect_uri", yahooProperties.getOauth().getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("state", state)
                .queryParam("scope", "openid")
                .build(true)
                .toUriString();
    }


    public YahooTokenResponse exchangeCodeForToken(String code){

        String basic = Base64.getEncoder().encodeToString((yahooProperties.getOauth().getClientId() + ":" + yahooProperties.getOauth().getClientSecret()).getBytes());

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

        form.add("grant_type", "authorization_code");
        form.add("code", code);
        form.add("redirect_uri", yahooProperties.getOauth().getRedirectUri());

        return oauthClient.post()
                .uri(yahooProperties.getOauth().getTokenUrl())
                .header("Authorization", "Basic " + basic)
                .body(BodyInserters.fromFormData(form))
                .retrieve()
                .bodyToMono(YahooTokenResponse.class)
                .block();

    }

    public YahooTokenResponse refreshToken(String refreshToken){
        String basic = Base64.getEncoder().encodeToString((yahooProperties.getOauth().getClientId() + ":" + yahooProperties.getOauth().getClientSecret()).getBytes());

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", refreshToken);

        return oauthClient.post()
                .uri(yahooProperties.getOauth().getTokenUrl())
                .header("Authorization", "Basic " + basic)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData(form))
                .retrieve()
                .bodyToMono(YahooTokenResponse.class)
                .block();
    }


}



