package com.behl.ehrmantraut.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.behl.ehrmantraut.dto.UserCreationRequestDto;
import com.behl.ehrmantraut.service.UserService;
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

}
