package org.example.gym.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.TokenEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
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
 * and it supports automatic expiration and invalidation of tokens. The secret key is generated
 * using a secure algorithm and is encoded in Base64 format.</p>
 */
@Component
@Slf4j
public class JwtUtils {

    @Value("${spring.jwt.secret}")
    private String secretKey;
    private final TokenService tokenService;

    /**
     * Constructs a JwtUtils object, generating a secret key for signing JWT tokens.
     *
     * <p>The secret key is generated using the specified algorithm in the {@link SecurityConstants#KEY_GEN}
     * constant and encoded in Base64 format. The key is securely generated using {@link KeyGenerator}.</p>
     *
     * @param tokenService The TokenService used to manage tokens.
     */
    public JwtUtils(TokenService tokenService) {
        this.tokenService = tokenService;
    }



    /**
     * Generates a JWT token for the given user details.
     *
     * <p>The generated token includes the username (subject), the issued date, and an expiration
     * date set according to {@link SecurityConstants#EXPIRATION_TIME}. The token is signed using
     * the securely generated secret key.</p>
     *
     * @param userDetails the user details for whom the token is to be generated.
     * @return the generated JWT token as a string.
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim(SecurityConstants.ROLES, userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(getKey())
                .compact();
    }

    /**
     * Checks if a given JWT token has been revoked.
     *
     * <p>This method queries the TokenService to check if the token is revoked by looking it up in
     * the repository. If the token is found and marked as revoked, it returns true. If not found,
     * it assumes the token is revoked.</p>
     *
     * @param token the JWT token to check.
     * @return true if the token is revoked, false otherwise.
     */
    public boolean isTokenRevoked(String token) {
        return tokenService.getByToken(token)
                .map(TokenEntity::isRevoked)
                .orElse(true);
    }


    /**
     * Retrieves the secret key used for signing and verifying JWT tokens.
     *
     * <p>This method decodes the Base64 encoded secret key into a {@link SecretKey} that is used
     * for signing JWT tokens.</p>
     *
     * @return the {@link SecretKey} used for JWT signing and verification.
     */
    public SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Revokes all tokens associated with the given user.
     *
     * <p>This method retrieves all tokens belonging to the specified user and marks them as revoked.
     * It updates the revoked status of each token and saves the changes to the database.</p>
     *
     * @param user the {@link UserEntity} whose tokens are to be revoked.
     */
    public void revokeUserToken(UserEntity user) {
        TokenEntity tokenByUser = tokenService.getTokenByUser(user);
        tokenByUser.setRevoked(true);
        tokenService.save(tokenByUser);
        log.debug("token for user {} have been revoked.", user.getUsername());
    }

}
