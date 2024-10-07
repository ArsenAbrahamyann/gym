package org.example.gym.repository;

import java.util.List;
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
     * Finds all usernames of the users in the system.
     *
     * @return a list of usernames, or an empty list if none found
     */
    @Query("SELECT u.username FROM UserEntity u")
    List<String> findAllUsername();


}
