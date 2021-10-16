package com.behl.ehrmantraut.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.behl.ehrmantraut.constant.ApiPath;
import com.behl.ehrmantraut.constant.ApiStatusDescription;
import com.behl.ehrmantraut.constant.ApiSummary;
import com.behl.ehrmantraut.utility.ResponseProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class JokeController {

    @GetMapping(value = ApiPath.JOKE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = ApiStatusDescription.JOKE_RETREIVAL),
            @ApiResponse(responseCode = "401", description = ApiStatusDescription.NO_TOKEN_ACCESS) })
    @Operation(summary = ApiSummary.JOKE_RETREIVAL)
    public ResponseEntity<?> jokeRetreivalHandler() {
        return ResponseEntity.ok(ResponseProvider.generateJoke());
    }

}
