package com.example.fbyahoo.exception;

import com.example.fbyahoo.enums.OAuthFailureReason;
import lombok.Getter;

@Getter
public class OAuthFlowException extends RuntimeException{

    private final OAuthFailureReason reason;


    public OAuthFlowException(OAuthFailureReason reason, String message) {
        // passes message to runtime exception constructor
        super(message);
        this.reason = reason;
    }

    public OAuthFlowException(OAuthFailureReason reason, String message, Throwable cause) {
        super(message, cause);
        this.reason = reason;
    }

}
