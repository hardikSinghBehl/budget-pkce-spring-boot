package com.behl.ehrmantraut.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class UserCreationRequestDto {

    @Size(max = 100, message = "fullname cannot exceed 100 chracters")
    @Schema(description = "fullname of user", example = "Mike Ehrmantraut")
    private final String fullName;

    @Email(message = "email-id must be of valid format")
    @NotBlank(message = "email-id must not be empty")
    @Size(max = 50, message = "email-id cannot exceed 50 chracters")
    @Schema(description = "Email-id of user", example = "mike.ehrmantraut@gmail.com")
    private final String emailId;

    @NotBlank(message = "password must not be empty")
    @Size(max = 30, message = "password cannot exceed 30 chracters")
    @Schema(description = "Password of user", example = "noHalfMeasures")
    private final String password;

}
