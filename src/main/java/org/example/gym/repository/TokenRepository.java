package org.example.gym.repository;

import java.util.List;
import java.util.Optional;
import org.example.gym.entity.TokenEntity;
import org.example.gym.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for performing CRUD operations on {@link TokenEntity} objects.
 *
 * <p>This interface extends {@link JpaRepository}, providing built-in methods for interacting with the database.
 * It also defines custom queries to retrieve token data based on user and token attributes.</p>
 */
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    /**
     * Finds all valid (non-revoked) tokens associated with a specific user.
     *
     * <p>This query joins the {@link TokenEntity} with {@link UserEntity} based on the user's ID
     * and returns a list of tokens that are not revoked.</p>
     *
     * @param userId the ID of the user whose valid tokens are to be retrieved
     * @return a list of {@link TokenEntity} objects that are valid for the specified user
     */
    @Query("""
                SELECT t FROM TokenEntity t 
                INNER JOIN t.user u 
                WHERE u.id = :userId AND t.revoked = false
            """)
    List<TokenEntity> findAllValidTokensByUser(Long userId);

    /**
     * Finds a token by its value.
     *
     * <p>This method returns an {@link Optional} containing the {@link TokenEntity} with the given token value,
     * or an empty {@link Optional} if no such token exists.</p>
     *
     * @return an {@link Optional} containing the {@link TokenEntity} if found, or empty if not
     */
    Optional<TokenEntity> findByToken(String tokenEntity);

    /**
     * Finds all tokens associated with a specific user.
     *
     * <p>This method retrieves all {@link TokenEntity} objects linked to a given {@link UserEntity}.</p>
     *
     * @param user the user whose tokens are to be retrieved
     * @return a list of {@link TokenEntity} objects associated with the specified user
     */
    @Query("SELECT t FROM TokenEntity t WHERE t.user = :user AND t.revoked = false")
    TokenEntity findByUser(@Param("user") UserEntity user);
}
