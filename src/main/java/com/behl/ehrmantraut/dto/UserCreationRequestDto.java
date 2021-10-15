package com.behl.ehrmantraut.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class UserCreationRequestDto {

    private final String fullName;
    private final String emailId;
    private final String password;

}
