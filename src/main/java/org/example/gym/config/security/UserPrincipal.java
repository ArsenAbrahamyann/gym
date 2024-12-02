package org.example.gym.config.security;

import java.util.Collection;
import java.util.Collections;
import org.example.gym.entity.UserEntity;
import org.example.gym.entity.enums.ERole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Custom implementation of the {@link UserDetails} interface for representing a user in the Spring Security context.
 *
 * <p>This class encapsulates a {@link UserEntity} and provides the necessary information about the user's
 * credentials, authorities, and account status. It is used by Spring Security to authenticate and authorize
 * users based on their roles and other details.</p>
 */
public class UserPrincipal implements UserDetails {
    private final UserEntity user;

    /**
     * Constructor for creating a {@link UserPrincipal} object from a {@link UserEntity}.
     *
     * @param user the {@link UserEntity} object representing the user
     */
    public UserPrincipal(UserEntity user) {
        this.user = user;
    }

    /**
     * Returns the authorities granted to the user based on their role.
     *
     * <p>The authorities are returned as a collection of {@link GrantedAuthority} objects. In this implementation,
     * the user can have either "ROLE_TRAINER" or "ROLE_TRAINEE" as their granted authority, depending on their role.</p>
     *
     * @return a collection of {@link GrantedAuthority} objects representing the user's roles
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRole().equals(ERole.ROLE_TRAINER)) {
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_TRAINER"));
        } else if (user.getRole().equals(ERole.ROLE_TRAINEE)) {
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_TRAINEE"));
        }
        return Collections.emptyList();
    }

    /**
     * Returns the user's password.
     *
     * @return the password associated with the user
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username of the user.
     *
     * @return the username associated with the user
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return {@code true} if the account is not expired, {@code false} otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     *
     * @return {@code true} if the account is not locked, {@code false} otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    /**
     * Indicates whether the user's credentials have expired.
     *
     * @return {@code true} if the credentials are not expired, {@code false} otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is enabled.
     *
     * @return {@code true} if the account is enabled, {@code false} otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
