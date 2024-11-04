package org.example.gym.security;

import java.util.Optional;
import org.example.gym.entity.UserEntity;
import org.example.gym.repository.UserRepository;
import org.example.gym.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final LoginAttemptService attemptService;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, LoginAttemptService attemptService) {
        this.userRepository = userRepository;
        this.attemptService = attemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (attemptService.isBlocked(username)) {
            throw new LockedException("User is temporarily locked due to multiple failed login attempts.");
        }

        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            attemptService.loginFailed(username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        attemptService.loginSucceeded(username);

        UserEntity user = optionalUser.get();

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getIsActive(),
                true,
                true,
                true,
                AuthorityUtils.createAuthorityList("ROLE_USER")
        );
    }
}
