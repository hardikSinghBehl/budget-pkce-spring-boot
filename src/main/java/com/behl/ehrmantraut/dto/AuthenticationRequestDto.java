package com.behl.ehrmantraut.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class AuthenticationRequestDto {

    private final String emailId;
    private final String password;
    private final String clientId;
    private final String responseType;
    private final String redirectUri;
    private final String codeChallengeMethod;
    private final String codeChallenge;
    private final String state;

}
