package com.behl.ehrmantraut.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class UserAuthenticationDto {

    private final UUID userId;
    private final AuthenticationRequestDto authentication;

}
