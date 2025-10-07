package com.example.healthcare.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

public class JwtUtils {

    private static final String PUBLIC_KEY_STRING =
            "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkq...\n-----END PUBLIC KEY-----";

    private static PublicKey getPublicKey() {
        try {
            String key = PUBLIC_KEY_STRING
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] bytes = Base64.getDecoder().decode(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
            return KeyFactory.getInstance("RSA").generatePublic(spec);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key", e);
        }
    }

    private static Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getPublicKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new JwtException("Invalid or expired JWT token", e);
        }
    }

    public static Long extractUserIdFromToken(String token) {
        Object idObj = parseToken(token).get("id");
        if (idObj instanceof Number) {
            return ((Number) idObj).longValue();
        } else if (idObj instanceof String) {
            return Long.parseLong((String) idObj);
        } else {
            throw new RuntimeException("JWT 'id' claim is missing or invalid");
        }
    }

    public static String extractEmailFromToken(String token) {
        return parseToken(token).get("email", String.class);
    }

    @SuppressWarnings("unchecked")
    public static List<String> extractRolesFromToken(String token) {
        return parseToken(token).get("roles", List.class);
    }
}
