package org.example.gym.aspects;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.gym.exeption.UnauthorizedException;
import org.example.gym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * An aspect that handles authentication for methods annotated with {@link org.example.gym.annotation.Authenticated}.
 * This aspect intercepts the method calls, retrieves authentication headers from the HTTP request,
 * and validates the user's credentials using the {@link UserService}.
 * <p>
 * If the authentication fails, an {@link UnauthorizedException} is thrown to indicate invalid credentials.
 * </p>
 */
@Aspect
@Component
public class AuthAspect {

    @Autowired
    private UserService userService;

    /**
     * Intercepts methods annotated with {@link org.example.gym.annotation.Authenticated} and performs
     * authentication by checking the provided username and password headers.
     *
     * @param joinPoint the join point providing reflective access to both the state available at a
     *                  join point and static information about it (e.g., method signature)
     * @throws Throwable if authentication fails or headers are missing
     */
    @Before("@annotation(org.example.gym.annotation.Authenticated)")
    public void authenticate(JoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();

        String headerUsername = request.getHeader("Username");
        String password = request.getHeader("Password");

        if (headerUsername != null && password != null) {
            boolean isAuthenticated = userService.authenticateUser(headerUsername, password);
            if (!isAuthenticated) {
                throw new UnauthorizedException("Invalid username or password");
            }
        } else {
            throw new UnauthorizedException("Missing authentication headers");
        }
    }

}
