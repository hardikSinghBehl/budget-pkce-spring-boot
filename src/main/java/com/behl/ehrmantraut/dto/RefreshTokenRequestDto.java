package com.behl.ehrmantraut.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class RefreshTokenRequestDto {

    private final String grantType;
    private final String refreshToken;
    private final String clientId;

}
