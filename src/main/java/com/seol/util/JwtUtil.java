package com.seol.util;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.seol.dto.ResponseCode;
import com.seol.exception.custom.UserLoginException;

public class JwtUtil {
    private final static String KEY = "HELLOKAKAOBANK";

    public static String createToken(String id) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY);
            return JWT.create().withIssuer("me")
                    .withExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000))).withClaim("id", id)
                    .sign(algorithm);
        } catch (Exception e) {
            throw new UserLoginException(ResponseCode.TOKEN_ERROR);
        }
    }

    public static String verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("me").acceptExpiresAt(5 * 60).build();
            return verifier.verify(token).getClaim("id").asString();
        } catch (Exception e) {
            throw new UserLoginException(ResponseCode.TOKEN_ERROR);
        }
    }
}
