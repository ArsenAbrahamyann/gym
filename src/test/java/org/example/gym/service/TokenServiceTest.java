package org.example.gym.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.gym.entity.TokenEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    private UserEntity mockUser;
    private TokenEntity mockToken;

    /**
     * Initializes mock objects for testing purposes.
     *
     * <p>This method sets up the initial state required for unit tests by creating mock instances of
     * {@code UserEntity} and {@code TokenEntity}. These mock objects simulate real entities
     * to isolate and test the functionality of the system under test without relying on actual database
     * interactions or external dependencies.</p>
     *
     * <p>The {@code UserEntity} is initialized with a sample ID. The {@code TokenEntity} is initialized
     * with a sample ID, token string, a reference to the mock {@code UserEntity}, and a revoked status
     * set to {@code false}.</p>
     *
     * <p>Called before each test method execution to ensure a clean and consistent state for the tests.</p>
     */
    @BeforeEach
    public void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(1L);

        mockToken = new TokenEntity();
        mockToken.setId(1L);
        mockToken.setToken("sampleToken");
        mockToken.setUser(mockUser);
        mockToken.setRevoked(false);
    }

    @Test
    public void testGetTokenByTokenString_Found() {
        when(tokenRepository.findByToken("sampleToken")).thenReturn(Optional.of(mockToken));

        Optional<TokenEntity> token = tokenService.getByToken("sampleToken");

        assertTrue(token.isPresent());
        assertEquals(mockToken, token.get());
        verify(tokenRepository).findByToken("sampleToken");
    }

    @Test
    public void testGetTokenByTokenString_NotFound() {
        when(tokenRepository.findByToken("invalidToken")).thenReturn(Optional.empty());

        Optional<TokenEntity> token = tokenService.getByToken("invalidToken");

        assertTrue(token.isEmpty());
        verify(tokenRepository).findByToken("invalidToken");
    }

    @Test
    public void testAddToken() {
        when(tokenRepository.save(mockToken)).thenReturn(mockToken);

        TokenEntity savedToken = tokenService.save(mockToken);

        assertNotNull(savedToken);
        assertEquals(mockToken, savedToken);
        verify(tokenRepository).save(mockToken);
    }

    @Test
    public void testDeleteToken() {
        doNothing().when(tokenRepository).delete(mockToken);

        tokenService.deleteToken(mockToken);

        verify(tokenRepository).delete(mockToken);
    }
}
