package com.example.fbyahoo.controller;

import com.example.fbyahoo.dto.YahooTokenResponse;
import com.example.fbyahoo.enums.OAuthFailureReason;
import com.example.fbyahoo.exception.OAuthFlowException;
import com.example.fbyahoo.service.TokenService;
import com.example.fbyahoo.service.YahooOAuthService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/oauth/yahoo")
public class YahooOAuthController {

    private final YahooOAuthService yahooOAuthService;
    private final TokenService tokenService;

    YahooOAuthController(YahooOAuthService yahooOAuthService, TokenService tokenService) {
        this.yahooOAuthService = yahooOAuthService;
        this.tokenService = tokenService;
    }

    @GetMapping("/login")
    public void redirectToYahoo(HttpServletResponse response, HttpSession session) throws IOException {

        String state = UUID.randomUUID().toString();
        session.setAttribute("state", state);
        response.sendRedirect(yahooOAuthService.buildAuthorizeUrl(state));

    }

    @GetMapping("/callback")
    public String handleYahooCallback(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session) {
        Object expected = session.getAttribute("state");
        if (!(expected instanceof String expectedState) || !expectedState.equals(state)) {
            throw new OAuthFlowException(OAuthFailureReason.STATE_MISMATCH, "State mismatch");
        }

        session.removeAttribute("state");
        YahooTokenResponse token = yahooOAuthService.exchangeCodeForToken(code);
        tokenService.saveToken(token);

        return "redirect:/";
    }




}
