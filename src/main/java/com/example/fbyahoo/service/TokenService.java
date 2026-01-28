package com.example.fbyahoo.service;


import com.example.fbyahoo.dto.YahooTokenResponse;
import com.example.fbyahoo.enums.OAuthFailureReason;
import com.example.fbyahoo.exception.OAuthFlowException;
import com.example.fbyahoo.model.OAuthToken;
import com.example.fbyahoo.repo.TokenRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final YahooOAuthService yahooOAuthService;

    public TokenService(TokenRepository tokenRepository, YahooOAuthService yahooOAuthService) {
        this.tokenRepository = tokenRepository;
        this.yahooOAuthService = yahooOAuthService;
    }


    public Optional<OAuthToken> getToken() {
        return tokenRepository.findTopByOrderByIdAsc();
    }

    private boolean isExpiredOrMissing() {
        return getToken().map(this::isExpired).orElse(true);
    }

    private boolean isExpired(OAuthToken token) {
        return token.getExpiresAt().isBefore(Instant.now().plusSeconds(30));
    }

    private Instant computeExpiresAt(Long expiresIn) {
        return Instant.now().plusSeconds(expiresIn - 30);
    }

    @Transactional
    public String getValidAccessToken() {
        OAuthToken token = getToken().orElseThrow(() -> new OAuthFlowException(
                OAuthFailureReason.TOKEN_MISSING,
                "No token found in database"
        ));

        if (!isExpired(token)) {
            return token.getAccessToken();
        }
        try{
            YahooTokenResponse tokenResponse = yahooOAuthService.refreshToken(token.getRefreshToken());
            saveToken(tokenResponse);
            return tokenResponse.accessToken();
        } catch (Exception e) {
            tokenRepository.deleteAll();
            throw new OAuthFlowException(OAuthFailureReason.REFRESH_FAILED, "Failed to refresh token. Login needed", e);
        }

    }


    @Transactional
    public void saveToken(YahooTokenResponse tokenResponse) {
        OAuthToken token = getToken().orElseGet(OAuthToken::new);

        token.setAccessToken(tokenResponse.accessToken());

        if (tokenResponse.refreshToken() != null && !tokenResponse.refreshToken().isBlank()) {
            token.setRefreshToken(tokenResponse.refreshToken());
        }
        token.setExpiresAt(computeExpiresAt(tokenResponse.expiresIn()));
        token.setTokenType(tokenResponse.tokenType());
        token.setScope(tokenResponse.scope());

        tokenRepository.save(token);


    }


}
