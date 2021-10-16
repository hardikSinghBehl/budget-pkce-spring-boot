package com.behl.ehrmantraut.service;

import java.util.Map;

import com.behl.ehrmantraut.dto.AuthenticationRequestDto;
import com.behl.ehrmantraut.dto.AuthenticationSuccessDto;
import com.behl.ehrmantraut.dto.CodeExchangeRequestDto;
import com.behl.ehrmantraut.dto.RefreshTokenRequestDto;
import com.behl.ehrmantraut.dto.UserCreationRequestDto;

public interface UserService {

    void create(final UserCreationRequestDto userCreationRequestDto);

    Map<String, String> authenticate(final AuthenticationRequestDto authenticationRequestDto);

    AuthenticationSuccessDto exchangeCode(final CodeExchangeRequestDto codeExchangeRequestDto);

    AuthenticationSuccessDto refreshToken(final RefreshTokenRequestDto refreshTokenRequestDto);

}