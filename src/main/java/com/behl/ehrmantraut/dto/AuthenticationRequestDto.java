package com.behl.ehrmantraut.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class AuthenticationRequestDto {

    @Email(message = "email-id must be of valid format")
    @Schema(description = "Email-id of user", example = "mike.ehrmantraut@gmail.com")
    @NotBlank(message = "email-id must not be empty")
    private final String emailId;

    @Schema(description = "Password of user", example = "noHalfMeasures")
    @NotBlank(message = "password must not be empty")
    private final String password;

    @Schema(description = "client-id as configured")
    @NotBlank(message = "clientId must not be empty")
    private final String clientId;

    @Schema(description = "response_type expected", example = "code")
    @NotBlank(message = "responseType must not be empty")
    private final String responseType;

    @Schema(description = "redirect_uri as configured")
    @NotBlank(message = "redirectUri must not be empty")
    private final String redirectUri;

    @Schema(description = "method used to calculate codeChallenge: must be S256", example = "S256")
    @NotBlank(message = "codeChallengeMethod must not be empty")
    private final String codeChallengeMethod;

    @Schema(description = "code_challenge value as calculated from code_verifier", example = "Ijcr0PLd8HvnhB9AZXlhmPPJjyLyaPkianM0ERzD860")
    @NotBlank(message = "codeChallenge must not be empty")
    private final String codeChallenge;

    @Schema(description = "Recommended: This can be used to mitigate cross-site request forgery attacks.")
    private final String state;

}
