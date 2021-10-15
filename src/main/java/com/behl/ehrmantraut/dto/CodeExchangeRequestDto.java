package com.behl.ehrmantraut.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class CodeExchangeRequestDto {

    private final String clientId;
    private final String grantType;
    private final String code;
    private final String redirectUri;
    private final String codeVerifier;

}
