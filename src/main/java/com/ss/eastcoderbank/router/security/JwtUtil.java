package com.ss.eastcoderbank.router.security;

import lombok.Getter;

@Getter
public enum JwtUtil {

    JWT_UTIL(864000000, "Bearer ", "Authorization");

    private final Integer expirationTime;
    private final String tokenPrefix;
    private final String header;

    JwtUtil(Integer expirationTime, String tokenPrefix, String header) {
        this.expirationTime = expirationTime;
        this.tokenPrefix = tokenPrefix;
        this.header = header;
    }
}