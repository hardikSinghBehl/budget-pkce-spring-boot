package com.behl.ehrmantraut.service.implementation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.behl.ehrmantraut.dto.AuthenticationRequestDto;
import com.behl.ehrmantraut.dto.AuthenticationSuccessDto;
import com.behl.ehrmantraut.dto.CodeExchangeRequestDto;
import com.behl.ehrmantraut.dto.RefreshTokenRequestDto;
import com.behl.ehrmantraut.dto.UserAuthenticationDto;
import com.behl.ehrmantraut.dto.UserCreationRequestDto;
import com.behl.ehrmantraut.entity.User;
import com.behl.ehrmantraut.exception.CodeChallengeAndVerifierMismatchException;
import com.behl.ehrmantraut.exception.DuplicateEmailIdException;
import com.behl.ehrmantraut.exception.GenericBadRequestException;
import com.behl.ehrmantraut.exception.GenericUnauthorizedExcpetion;
import com.behl.ehrmantraut.exception.InvalidCodeException;
import com.behl.ehrmantraut.exception.InvalidCredentialsException;
import com.behl.ehrmantraut.repository.UserRepository;
import com.behl.ehrmantraut.security.configuration.properties.PkceConfigurationProperties;
import com.behl.ehrmantraut.security.configuration.properties.PkceConfigurationProperties.Security;
import com.behl.ehrmantraut.security.utility.JwtUtils;
import com.behl.ehrmantraut.service.UserService;
import com.behl.ehrmantraut.utility.CodeUtility;
import com.google.common.cache.LoadingCache;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@EnableConfigurationProperties(value = PkceConfigurationProperties.class)
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoadingCache<String, UserAuthenticationDto> codeCache;
    private final PkceConfigurationProperties pkceConfigurationProperties;
    private final JwtUtils jwtUtils;

    @Override
    public void create(final UserCreationRequestDto userCreationRequestDto) {
        if (!isEmailIdUnique(userCreationRequestDto.getEmailId()))
            throw new DuplicateEmailIdException();

        final var user = new User();
        user.setFullName(userCreationRequestDto.getFullName());
        user.setEmailId(userCreationRequestDto.getEmailId());
        user.setPassword(passwordEncoder.encode(userCreationRequestDto.getPassword()));
        userRepository.save(user);
    }

    private boolean isEmailIdUnique(final String emailId) {
        return !userRepository.existsByEmailId(emailId);
    }

    @Override
    public Map<String, String> authenticate(final AuthenticationRequestDto authenticationRequestDto) {
        final var user = userRepository.findByEmailId(authenticationRequestDto.getEmailId())
                .orElseThrow(() -> new InvalidCredentialsException());

        if (!passwordEncoder.matches(authenticationRequestDto.getPassword(), user.getPassword()))
            throw new InvalidCredentialsException();

        final var securityProperties = pkceConfigurationProperties.getSecurity();
        validateAuthenticationRequest(authenticationRequestDto, securityProperties);

        final String generatedCode = CodeUtility.generateCode();
        codeCache.put(generatedCode,
                UserAuthenticationDto.builder().userId(user.getId()).authentication(authenticationRequestDto).build());

        final var response = new HashMap<String, String>();
        response.put("code", generatedCode);
        response.put("state", authenticationRequestDto.getState());
        return response;
    }

    @Override
    public AuthenticationSuccessDto exchangeCode(final CodeExchangeRequestDto codeExchangeRequestDto) {
        final var securityProperties = pkceConfigurationProperties.getSecurity();
        validateCodeExchangeRequest(codeExchangeRequestDto, securityProperties);

        UserAuthenticationDto userAuthenticationRequest;
        try {
            if (codeCache.get(codeExchangeRequestDto.getCode()) != null)
                userAuthenticationRequest = codeCache.get(codeExchangeRequestDto.getCode());
            else
                throw new InvalidCodeException();
        } catch (ExecutionException e) {
            log.error("Unable to fetch code: ", e);
            throw new InvalidCodeException();
        }
        codeCache.invalidate(codeExchangeRequestDto.getCode());

        final var generatedCodeChallenge = CodeUtility.codeChallengeGenerator()
                .generate(codeExchangeRequestDto.getCodeVerifier());

        if (!generatedCodeChallenge.equals(userAuthenticationRequest.getAuthentication().getCodeChallenge()))
            throw new CodeChallengeAndVerifierMismatchException();

        final var user = userRepository.findById(userAuthenticationRequest.getUserId()).get();
        return AuthenticationSuccessDto.builder().accessToken(jwtUtils.generateAccessToken(user))
                .refreshToken(jwtUtils.generateRefreshToken(user)).tokenType("Bearer")
                .expiresIn(TimeUnit.HOURS.toSeconds(1)).build();
    }

    @Override
    public AuthenticationSuccessDto refreshToken(final RefreshTokenRequestDto refreshTokenRequestDto) {
        if (jwtUtils.isTokenExpired(refreshTokenRequestDto.getRefreshToken()))
            throw new GenericUnauthorizedExcpetion();
        final var user = userRepository.findByEmailId(jwtUtils.extractEmail(refreshTokenRequestDto.getRefreshToken()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        return AuthenticationSuccessDto.builder().accessToken(jwtUtils.generateAccessToken(user))
                .refreshToken(refreshTokenRequestDto.getRefreshToken()).tokenType("Bearer")
                .expiresIn(TimeUnit.HOURS.toSeconds(1)).build();
    }

    private void validateCodeExchangeRequest(final CodeExchangeRequestDto codeExchangeRequestDto,
            final Security securityProperties) {
        if (!codeExchangeRequestDto.getClientId().equals(securityProperties.getClientId()))
            throw new GenericBadRequestException("Invalid client-id");
        if (!codeExchangeRequestDto.getGrantType().equals(securityProperties.getGrantType()))
            throw new GenericBadRequestException("Invalid grant-type value");
        if (!codeExchangeRequestDto.getRedirectUri().equals(securityProperties.getRedirectUri()))
            throw new GenericBadRequestException("Invalid redirect-uri");
    }

    private void validateAuthenticationRequest(final AuthenticationRequestDto authenticationRequestDto,
            final Security securityProperties) {
        if (!authenticationRequestDto.getClientId().equals(securityProperties.getClientId()))
            throw new GenericBadRequestException("Invalid client-id");
        if (!authenticationRequestDto.getResponseType().equals(securityProperties.getResponseType()))
            throw new GenericBadRequestException("Invalid response-type value");
        if (!authenticationRequestDto.getRedirectUri().equals(securityProperties.getRedirectUri()))
            throw new GenericBadRequestException("Invalid redirect-uri");
        if (!authenticationRequestDto.getCodeChallengeMethod().equals(securityProperties.getCodeChallengeMethod()))
            throw new GenericBadRequestException("Invalid code-challenge-method");
    }

}
