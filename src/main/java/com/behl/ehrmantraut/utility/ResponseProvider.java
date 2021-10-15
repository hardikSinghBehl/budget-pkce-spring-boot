package com.behl.ehrmantraut.utility;

import java.util.HashMap;
import java.util.Map;

public class ResponseProvider {

    public static Map<String, String> userCreationSuccess() {
        final var response = new HashMap<String, String>();
        response.put("Message", "Account created successfully!");
        return response;
    }

    public static Map<String, String> generateJoke() {
        final var response = new HashMap<String, String>();
        response.put("Joke", "Chameleons are supposed to blend well, but I think it's ruined this smoothie.");
        return response;
    }

}
