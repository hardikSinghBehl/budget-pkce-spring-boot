package com.behl.ehrmantraut.controller;

import javax.validation.Valid;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.behl.ehrmantraut.constant.ApiPath;
import com.behl.ehrmantraut.constant.ApiStatusDescription;
import com.behl.ehrmantraut.constant.ApiSummary;
import com.behl.ehrmantraut.dto.AuthenticationRequestDto;
import com.behl.ehrmantraut.dto.AuthenticationSuccessDto;
import com.behl.ehrmantraut.dto.CodeExchangeRequestDto;
import com.behl.ehrmantraut.dto.RefreshTokenRequestDto;
import com.behl.ehrmantraut.dto.UserCreationRequestDto;
import com.behl.ehrmantraut.exception.GenericBadRequestException;
import com.behl.ehrmantraut.security.configuration.properties.PkceConfigurationProperties;
import com.behl.ehrmantraut.service.UserService;
import com.behl.ehrmantraut.utility.ObjectConverter;
import com.behl.ehrmantraut.utility.RedirectUriBuilder;
import com.behl.ehrmantraut.utility.RequestReader;
import com.behl.ehrmantraut.utility.ResponseProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@EnableConfigurationProperties(value = PkceConfigurationProperties.class)
public class AuthenticationController {

    private final UserService userService;
    private final PkceConfigurationProperties pkceConfigurationProperties;
    private final ObjectConverter objectConverter;
    private final RequestReader requestReader;

    @PostMapping(value = ApiPath.SIGN_UP, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ApiStatusDescription.USER_CREATION_SUCCESS),
            @ApiResponse(responseCode = "409", description = ApiStatusDescription.DUPLICATE_USER_EMAIL_ID) })
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = ApiSummary.USER_ACCOUNT_CREATION)
    public ResponseEntity<?> userCreationHandler(
            @Valid @RequestBody(required = true) final UserCreationRequestDto userCreationRequestDto) {
        userService.create(userCreationRequestDto);
        return ResponseEntity.ok(ResponseProvider.userCreationSuccess());
    }

    @PostMapping(value = ApiPath.AUTHENTICATE)
    @ApiResponses(value = { @ApiResponse(responseCode = "302", description = ApiStatusDescription.CODE_RETURNED),
            @ApiResponse(responseCode = "401", description = ApiStatusDescription.INVALID_USER_CREDENTIALS),
            @ApiResponse(responseCode = "400", description = ApiStatusDescription.BAD_REQUEST) })
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = ApiSummary.AUTHENTICATE_REDIRECTION)
    public ResponseEntity<?> userAuthenticationHandler(
            @Valid @RequestBody(required = true) final AuthenticationRequestDto authenticationRequestDto) {
        final var response = userService.authenticate(authenticationRequestDto);
        return ResponseEntity.status(HttpStatus.FOUND).location(RedirectUriBuilder
                .build(authenticationRequestDto.getRedirectUri(), response.get("code"), response.get("state"))).build();
    }

    @PostMapping(value = ApiPath.TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ApiStatusDescription.SUCCESSFULL_CODE_EXCHANGE),
            @ApiResponse(responseCode = "400", description = ApiStatusDescription.BAD_REQUEST),
            @ApiResponse(responseCode = "403", description = ApiStatusDescription.INVALID_CODE),
            @ApiResponse(responseCode = "412", description = ApiStatusDescription.CODE_VERIFIER_MISMATCH),
            @ApiResponse(responseCode = "401", description = ApiStatusDescription.REFRESH_TOKEN_EXPIRED) })
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = ApiSummary.TOKEN_EXCHANGE)
    public ResponseEntity<AuthenticationSuccessDto> tokenExchangeHandler(
            @Valid @RequestBody(required = true) final String request) {
        final var properties = pkceConfigurationProperties.getSecurity();
        String grantType = requestReader.readGrantType(request);
        if (grantType.equals(properties.getGrantType())) {
            return ResponseEntity
                    .ok(userService.exchangeCode(objectConverter.convert(request, CodeExchangeRequestDto.class)));
        } else if (grantType.equals(properties.getRefresh().getGrantType())) {
            return ResponseEntity.ok(
                    userService.refreshToken(objectConverter.convertRefresh(request, RefreshTokenRequestDto.class)));
        }
        throw new GenericBadRequestException("grantType contains unrecognizable value");
    }

}
