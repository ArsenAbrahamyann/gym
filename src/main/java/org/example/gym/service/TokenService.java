package org.example.gym.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.gym.entity.TokenEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.repository.TokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    
    private final TokenRepository tokenRepository;

    /**
     * Retrieves all valid tokens for a given user.
     *
     * @param userId The user ID to fetch valid tokens for.
     * @return A list of valid tokens for the specified user.
     */
    public List<TokenEntity> getAllValidTokensByUser(Long userId) {
        return tokenRepository.findAllValidTokensByUser(userId);
    }

    /**
     * Retrieves a token by its token string.
     *
     * @param token The token string to look for.
     * @return An Optional containing the token if found, or empty if not.
     */
    public Optional<TokenEntity> getTokenByTokenString(String token) {
        return tokenRepository.findByToken(token);
    }

    /**
     * Retrieves all tokens associated with a specific user.
     *
     * @param user The user entity whose tokens are being retrieved.
     * @return A list of tokens associated with the user.
     */
    public List<TokenEntity> getTokensByUser(UserEntity user) {
        return tokenRepository.findByUser(user);
    }

    /**
     * Revokes all tokens for the given user.
     *
     * @param user The user whose tokens should be revoked.
     */
    public void revokeAllTokensForUser(UserEntity user) {
        List<TokenEntity> tokens = getTokensByUser(user);
        tokens.forEach(token -> {
            token.setRevoked(true);
            tokenRepository.save(token);
        });
    }

    /**
     * Adds a new token to the repository.
     *
     * @param token The token to be added.
     * @return The added token.
     */
    public TokenEntity addToken(TokenEntity token) {
        return tokenRepository.save(token);
    }

    /**
     * Deletes a specific token from the repository.
     *
     * @param token The token to be deleted.
     */
    public void deleteToken(TokenEntity token) {
        tokenRepository.delete(token);
    }
}
