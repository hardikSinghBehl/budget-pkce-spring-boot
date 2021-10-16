package com.behl.ehrmantraut.utility;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.behl.ehrmantraut.exception.GenericBadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class RequestReader {

    private final ObjectMapper objectMapper;

    public String readGrantType(final String requestBody) {
        HashMap<String, String> request;
        try {
            request = objectMapper.readValue(requestBody, new TypeReference<HashMap<String, String>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Grant type not present in request: ", e);
            throw new GenericBadRequestException("Request Body not properly constructed, No grantType found");
        }
        return request.get("grantType");
    }

}
