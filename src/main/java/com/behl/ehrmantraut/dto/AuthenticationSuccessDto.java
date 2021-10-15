package com.behl.ehrmantraut.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class AuthenticationSuccessDto {

    private final String accessToken;
    private final String tokenType;
    private final String refreshToken;
    private final Long expiresIn;

}
