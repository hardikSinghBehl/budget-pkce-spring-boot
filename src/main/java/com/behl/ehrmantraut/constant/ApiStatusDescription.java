package com.behl.ehrmantraut.constant;

public class ApiStatusDescription {

    public static final String USER_CREATION_SUCCESS = "User record created successfully";
    public static final String DUPLICATE_USER_EMAIL_ID = "User already exists with given email-id";
    public static final String BAD_REQUEST = "Bad request";
    public static final String INVALID_USER_CREDENTIALS = "Invalid email-id/password provided";
    public static final String CODE_RETURNED = "Code returned after successfull auth in redirect-uri";
    public static final String INVALID_CODE = "Invalid code provided";
    public static final String CODE_VERIFIER_MISMATCH = "Code verifier does not correspond to earlier provided code challenge";
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token expired";
    public static final String SUCCESSFULL_CODE_EXCHANGE = "Token(s) are returned signifying successful authentication";
    public static final String JOKE_RETREIVAL = "Unfunny joke returned";
    public static final String NO_TOKEN_ACCESS = "AccessToken missing in headers";

}
