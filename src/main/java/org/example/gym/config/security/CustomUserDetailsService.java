package org.example.gym.config.security;

import org.example.gym.entity.UserEntity;
import org.example.gym.repository.UserRepository;
import org.example.gym.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of the {@link UserDetailsService} interface.
 * This service is responsible for loading user-specific data during the authentication process.
 * It retrieves user information from the {@link UserRepository} and returns a {@link UserPrincipal} object.
 *
 * <p>The {@link UserPrincipal} represents a custom implementation of {@link UserDetails} with user details
 * required by Spring Security.</p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Lazy
    private final UserService userService;

    /**
     * Constructs a {@link CustomUserDetailsService} with the provided {@link UserRepository}.
     *
     * @param userService the {@link UserRepository} used to load user data from the database
     */
    public CustomUserDetailsService(@Lazy UserService userService) {
        this.userService = userService;
    }

    /**
     * Loads a user by their username.
     *
     * <p>This method is used by Spring Security during the authentication process to retrieve user
     * information. It queries the {@link UserRepository} for the given username, and if the user is
     * found, it returns a {@link UserPrincipal} object representing the user details.</p>
     *
     * @param username the username of the user to be loaded
     * @return a {@link UserDetails} object containing the user's information
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.findByUsername(username);

        return new UserPrincipal(userEntity);
    }
}
