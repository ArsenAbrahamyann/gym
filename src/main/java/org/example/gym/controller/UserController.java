package org.example.gym.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.paylod.request.ChangeLoginRequestDto;
import org.example.gym.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public ResponseEntity<Void> login(@RequestParam String username,
                                      @RequestParam String password) {
        log.info("Controller: User login attempt for username: {}", username);
        boolean isAuthenticated = userService.authenticateUser(username, password);

        if (isAuthenticated) {
            log.info("Controller: User {} logged in successfully", username);
            return ResponseEntity.ok().build();
        } else {
            log.warn("Controller: User {} failed to log in", username);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/change/login")
    public ResponseEntity<Void> changeLogin(@RequestParam String username,
                                            @RequestParam String oldPassword,
                                            @RequestParam String newPassword) {
        log.info("Controller: Change login for user: {}", username);
        ChangeLoginRequestDto changeLoginRequestDto = new ChangeLoginRequestDto(username, oldPassword, newPassword);
        userService.changePassword(changeLoginRequestDto);
        log.info("Controller: User {} changed password successfully", username);
        return ResponseEntity.ok().build();
    }


}
