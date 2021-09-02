package com.ss.eastcoderbank.router.security;

import com.auth0.jwt.JWT;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtHelper {

    //@Value("${jwt.secret}")
    //private final String jwtSecret;

   // private Key key;

    //@PostConstruct
    //public void init(){
    //    this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    //}

   // public JwtHelper(@Value("${jwt.secret}") String jwtSecret) {
    //    this.jwtSecret = jwtSecret;
   // }

    public static Integer parseId(String token) {
        return Integer.valueOf(JWT.decode(token).getSubject());
    }

    public boolean isExpired(String token) {
        return (JWT.decode(token).getExpiresAt()).after(new Date());
    }

    public Claims getClaims(String token) {
        return (Claims) JWT.decode(token).getClaims().values();
    }

}
