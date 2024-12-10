package org.example.gym.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

/**
 * CustomJwtDecoder decorates the JwtDecoder to add token revocation checks
 * before delegating the actual decoding process.
 * If the token is revoked, a {@link JwtException} is thrown. Otherwise,
 * the decoding is handled by the provided {@link JwtDecoder}.
 */
@RequiredArgsConstructor
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {

    private final JwtDecoder delegateDecoder;
    private final JwtUtils jwtUtils;


    /**
     * Decodes the provided JWT token after checking if it is revoked.
     *
     * @param token the JWT token to decode
     * @return the decoded {@link Jwt}
     * @throws JwtException if the token is revoked or decoding fails
     */
    @Override
    public Jwt decode(String token) throws JwtException {
        if (jwtUtils.isTokenRevoked(token)) {
            log.warn("Token is revoked: {}", token);
            throw new JwtException("Authentication failed: Token is revoked.");
        }
        return delegateDecoder.decode(token);
    }
}
