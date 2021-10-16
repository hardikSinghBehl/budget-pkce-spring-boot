package com.behl.ehrmantraut.utility;

import org.springframework.stereotype.Component;

import com.behl.ehrmantraut.dto.CodeExchangeRequestDto;
import com.behl.ehrmantraut.dto.RefreshTokenRequestDto;
import com.behl.ehrmantraut.exception.GenericBadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class ObjectConverter {

    private final ObjectMapper objectMapper;

    public CodeExchangeRequestDto convert(final String codeExchangeRequestDto,
            final Class<CodeExchangeRequestDto> dtoClass) {
        try {
            return objectMapper.readValue(codeExchangeRequestDto, CodeExchangeRequestDto.class);
        } catch (JsonProcessingException e) {
            log.error("Unable to parse JSON to object: ", e);
            throw new GenericBadRequestException("Request Body not properly constructed");
        }
    }

    public RefreshTokenRequestDto convertRefresh(final String refreshTokenRequestDto,
            final Class<RefreshTokenRequestDto> dtoClass) {
        try {
            return objectMapper.readValue(refreshTokenRequestDto, RefreshTokenRequestDto.class);
        } catch (JsonProcessingException e) {
            log.error("Unable to parse JSON to object: ", e);
            throw new GenericBadRequestException("Request Body not properly constructed");
        }
    }

}
