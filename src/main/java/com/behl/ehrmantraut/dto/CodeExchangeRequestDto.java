package com.behl.ehrmantraut.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class CodeExchangeRequestDto {

    @Schema(description = "client-id as configured")
    @NotBlank(message = "clientId must not be empty")
    private final String clientId;

    @Schema(example = "authorization_code")
    @NotBlank(message = "grantType must not be empty")
    private final String grantType;

    @Schema(description = "code received from /authenticate endpoint")
    @NotBlank(message = "code must not be empty")
    private final String code;

    @Schema(description = "redirect_uri as configured")
    @NotBlank(message = "redirectUri must not be empty")
    private final String redirectUri;

    @Schema(description = "value from which codeChallenge was generated in the /authenticate endpoint", example = "dcFKDCmdcYmcmW6DXu2BfSrkGB1cKwFAI5Jv7he9RDo")
    @NotBlank(message = "codeVerifier must not be empty")
    private final String codeVerifier;

}
