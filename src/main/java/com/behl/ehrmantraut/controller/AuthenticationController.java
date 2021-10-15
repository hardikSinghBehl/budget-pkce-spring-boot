package com.behl.ehrmantraut.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.behl.ehrmantraut.dto.AuthenticationRequestDto;
import com.behl.ehrmantraut.dto.AuthenticationSuccessDto;
import com.behl.ehrmantraut.dto.CodeExchangeRequestDto;
import com.behl.ehrmantraut.dto.UserCreationRequestDto;
import com.behl.ehrmantraut.service.UserService;
import com.behl.ehrmantraut.utility.RedirectUriBuilder;
import com.behl.ehrmantraut.utility.ResponseProvider;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping(value = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Creates a user record in the system")
    public ResponseEntity<?> userCreationHandler(
            @RequestBody(required = true) final UserCreationRequestDto userCreationRequestDto) {
        userService.create(userCreationRequestDto);
        return ResponseEntity.ok(ResponseProvider.userCreationSuccess());
    }

    @PostMapping(value = "/authenticate")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Returns code that can be used to get access_token")
    public ResponseEntity<?> userAuthenticationHandler(
            @RequestBody(required = true) final AuthenticationRequestDto authenticationRequestDto) {
        final var response = userService.authenticate(authenticationRequestDto);
        return ResponseEntity.status(HttpStatus.FOUND).location(RedirectUriBuilder
                .build(authenticationRequestDto.getRedirectUri(), response.get("code"), response.get("state"))).build();
    }

    @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Returns token(s) in exchange of code")
    public ResponseEntity<AuthenticationSuccessDto> tokenExchangeHandler(
            @RequestBody(required = true) final CodeExchangeRequestDto codeExchangeRequestDto) {
        return ResponseEntity.ok(userService.exchangeCode(codeExchangeRequestDto));
    }

}
