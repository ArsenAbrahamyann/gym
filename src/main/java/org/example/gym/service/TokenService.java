package org.example.gym.service;

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
     * Retrieves a token by its token string.
     *
     * @param token The token string to look for.
     * @return An Optional containing the token if found, or empty if not.
     */
    public Optional<TokenEntity> getByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    /**
     * Retrieves all tokens associated with a specific user.
     *
     * @param user The user entity whose tokens are being retrieved.
     * @return A list of tokens associated with the user.
     */
    public TokenEntity getTokenByUser(UserEntity user) {
        return tokenRepository.findByUser(user);
    }

    /**
     * Adds a new token to the repository.
     *
     * @param token The token to be added.
     * @return The added token.
     */
    public TokenEntity save(TokenEntity token) {
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
