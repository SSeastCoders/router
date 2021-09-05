package com.ss.eastcoderbank.router.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class JwtHelper {

    private final String jwtSecret;

    public JwtHelper(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    // private Key key;

    //@PostConstruct
    //public void init(){
    //    this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    //}

   // public JwtHelper(@Value("${jwt.secret}") String jwtSecret) {
    //    this.jwtSecret = jwtSecret;
   // }

    //public static Integer parseId(String token) {
    //    return Integer.valueOf(JWT.decode(token).getSubject());
    //}

    public Integer parseId(String token) throws JWTDecodeException {
        token = token.replace(JwtUtil.JWT_UTIL.getTokenPrefix(), "");
        return Integer.valueOf(JWT.require(HMAC512(jwtSecret.getBytes())).build().verify(token).getSubject());
    }

    //public boolean isExpired(String token) {return (JWT.decode(token).getExpiresAt()).after(new Date()); }
    public boolean isExpired(String token) throws JWTDecodeException {
        //return (JWT.require(HMAC512(jwtSecret.getBytes())).build().verify(token).getExpiresAt()).after(new Date());
        token = token.replace(JwtUtil.JWT_UTIL.getTokenPrefix(), "");
        return (JWT.decode(token).getExpiresAt()).before(new Date());
    }

    //public Claims getClaims(String token) {
    //    return (Claims) JWT.decode(token).getClaims().values();
    //}

    public Collection<Claim> getClaims(String token) throws JWTDecodeException {
        token = token.replace(JwtUtil.JWT_UTIL.getTokenPrefix(), "");
        return JWT.require(HMAC512(jwtSecret.getBytes())).build().verify(token).getClaims().values();
    }

}
