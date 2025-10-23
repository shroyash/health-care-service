package com.example.healthcare.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private static PublicKey getPublicKey() {
        try {
            ClassPathResource resource = new ClassPathResource("public.pem");
            logger.debug("[JwtUtils] Checking if public.pem exists: {}", resource.exists());

            InputStream is = resource.getInputStream();
            logger.debug("[JwtUtils] Successfully opened public.pem input stream");

            String key = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            logger.debug("[JwtUtils] Public key after cleanup (first 50 chars): {}...",
                    key.substring(0, Math.min(50, key.length())));

            byte[] bytes = Base64.getDecoder().decode(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);
            logger.debug("[JwtUtils] Successfully generated PublicKey object");

            return publicKey;

        } catch (Exception e) {
            logger.error("[JwtUtils] Failed to load public key", e);
            throw new RuntimeException("Failed to load public key", e);
        }
    }

    private static Claims parseToken(String token) {
        try {
            logger.debug("[JwtUtils] Parsing JWT token: {}", token);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getPublicKey())
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
            logger.debug("[JwtUtils] JWT parsed successfully: {}", claims);
            return claims;

        } catch (JwtException e) {
            logger.error("[JwtUtils] Invalid or expired JWT token", e);
            throw new JwtException("Invalid or expired JWT token", e);
        }
    }

    public static Long extractUserIdFromToken(String token) {
        logger.debug("[JwtUtils] Extracting userId from token");
        Object idObj = parseToken(token).get("id");
        if (idObj instanceof Number) return ((Number) idObj).longValue();
        else if (idObj instanceof String) return Long.parseLong((String) idObj);
        else throw new RuntimeException("JWT 'id' claim is missing or invalid");
    }

    public static String extractEmailFromToken(String token) {
        return parseToken(token).get("email", String.class);
    }

    public static String extractUserNameFromToken(String token) {
        return parseToken(token).get("username", String.class);
    }

    @SuppressWarnings("unchecked")
    public static List<String> extractRolesFromToken(String token) {
        logger.debug("[JwtUtils] Extracting roles from token");
        return parseToken(token).get("roles", List.class);
    }
}


