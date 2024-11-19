package org.example.gym.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Utility class for generating, validating, and managing JSON Web Tokens (JWT).
 *
 * <p>This class provides methods to generate JWT tokens, validate their authenticity,
 * and manage token expiration and blacklisting. It also provides functionality to extract
 * user information from tokens and to invalidate tokens by adding them to a blacklist.</p>
 *
 * <p>The class uses a securely generated secret key for signing and verifying tokens,
 * and it supports automatic expiration and invalidation of tokens.</p>
 */
@Component
public class JwtUtils {

    private final String secretKey;
    private final ConcurrentHashMap<String, Long> tokenBlacklist = new ConcurrentHashMap<>();

    /**
     * Constructs a JwtUtils object, generating a secret key for signing JWT tokens.
     *
     * <p>The secret key is generated using the specified algorithm in the {@link SecurityConstants#KEY_GEN}
     * constant and encoded in Base64 format.</p>
     */
    public JwtUtils() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(SecurityConstants.KEY_GEN);
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a JWT token for the given user details.
     *
     * <p>The generated token includes the username (subject), the issued date, and an expiration
     * date set according to {@link SecurityConstants#EXPIRATION_TIME}.</p>
     *
     * @param userDetails the user details for whom the token is to be generated
     * @return the generated JWT token as a string
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(getKey())
                .compact();
    }

    /**
     * Validates the provided JWT token.
     *
     * <p>The token is considered valid if it has not expired, is not blacklisted, and the username
     * in the token matches the provided user details.</p>
     *
     * @param token the JWT token to be validated
     * @param userDetails the user details to be checked against the token
     * @return true if the token is valid, otherwise false
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token) && isTokenBlacklisted(token);
    }


    /**
     * Checks if the JWT token is blacklisted.
     *
     * <p>A token is considered blacklisted if it has been explicitly added to the blacklist.</p>
     *
     * @param jwt the JWT token to check
     * @return true if the token is not blacklisted, otherwise false
     */
    public boolean isTokenBlacklisted(String jwt) {
        return ! tokenBlacklist.containsKey(jwt);
    }


    /**
     * Retrieves the signing key used to sign and verify JWT tokens.
     *
     * @return the signing key used to sign and verify JWT tokens
     */
    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * Invalidates a given JWT token by adding it to the blacklist.
     *
     * <p>The token will be marked as expired, and its expiration time will be recorded in the blacklist.</p>
     *
     * @param jwt the JWT token to invalidate
     */
    public void invalidateToken(String jwt) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(jwt).getBody();
        long expiration = claims.getExpiration().getTime();
        tokenBlacklist.put(jwt, expiration);
    }

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token the JWT token from which the username is extracted
     * @return the username (subject) of the token
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Checks if the JWT token has expired.
     *
     * <p>A token is considered expired if the current date is after the expiration date of the token.</p>
     *
     * @param token the JWT token to check
     * @return true if the token is expired, otherwise false
     */
    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
