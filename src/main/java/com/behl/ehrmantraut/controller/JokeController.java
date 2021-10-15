package com.behl.ehrmantraut.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.behl.ehrmantraut.utility.ResponseProvider;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class JokeController {

    @GetMapping(value = "/joke", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Returns a not so funny joke")
    public ResponseEntity<?> jokeRetreivalHandler() {
        return ResponseEntity.ok(ResponseProvider.generateJoke());
    }

}
