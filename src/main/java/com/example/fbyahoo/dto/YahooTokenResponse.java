package com.example.fbyahoo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record YahooTokenResponse (

    @JsonProperty("access_token") String accessToken,

    @JsonProperty("refresh_token") String refreshToken,

    @JsonProperty("expires_in") Long expiresIn,

    @JsonProperty("token_type") String tokenType,

    @JsonProperty("scope") String scope

){}
