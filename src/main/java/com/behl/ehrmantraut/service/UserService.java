package com.behl.ehrmantraut.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.behl.ehrmantraut.dto.AuthenticationRequestDto;
import com.behl.ehrmantraut.dto.UserAuthenticationDto;
import com.behl.ehrmantraut.dto.UserCreationRequestDto;
import com.behl.ehrmantraut.entity.User;
import com.behl.ehrmantraut.exception.DuplicateEmailIdException;
import com.behl.ehrmantraut.exception.GenericBadRequestException;
import com.behl.ehrmantraut.exception.InvalidCredentialsException;
import com.behl.ehrmantraut.repository.UserRepository;
import com.behl.ehrmantraut.security.configuration.properties.PkceConfigurationProperties;
import com.behl.ehrmantraut.security.configuration.properties.PkceConfigurationProperties.Security;
import com.behl.ehrmantraut.utility.CodeUtility;
import com.google.common.cache.LoadingCache;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@EnableConfigurationProperties(value = PkceConfigurationProperties.class)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoadingCache<String, UserAuthenticationDto> codeCache;
    private final PkceConfigurationProperties pkceConfigurationProperties;

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
