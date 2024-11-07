package org.example.gym.repository;

import java.util.Optional;
import org.example.gym.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link UserEntity} operations.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Finds a {@link UserEntity} by its username.
     *
     * @param username the username of the user
     * @return an {@link Optional} containing the found {@link UserEntity}, or empty if not found
     */
    Optional<UserEntity> findByUsername(String username);


    /**
     * Exists By Username.
     */
    boolean existsByUsername(String username);


    /**
     * Custom query to get the count of usernames that start with the given prefix.
     *
     * @param prefix the prefix to search for
     * @return the count of usernames that start with the given prefix
     */
    @Query("SELECT COUNT(u.username) FROM UserEntity u WHERE u.username LIKE :prefix%")
    Integer countByUsernameStartingWith(String prefix);
}
