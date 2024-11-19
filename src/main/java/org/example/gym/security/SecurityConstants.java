package org.example.gym.security;

public class SecurityConstants {

    public static final String KEY_GEN = "HmacSHA256";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
    public static final long EXPIRATION_TIME = 600_000_000;
    public static final int MAX_ATTEMPTS = 3;
    public static final long BLOCK_DURATION = 5 * 60 * 1000;

}
