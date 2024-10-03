package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.paylod.request.ChangeLoginRequestDto;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> login(@RequestPart String username,
                                   @RequestPart String password) {
        log.info("Controller: Login user");
        boolean bool = userService.authenticateUser(username, password);
        if (bool) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/change/login")
    public ResponseEntity<?> changeLogin (@RequestPart String username,
                                          @RequestPart String oldPassword,
                                          @RequestPart String newPassword) {
        log.info("Controller: change login");
        ChangeLoginRequestDto changeLoginRequestDto = new ChangeLoginRequestDto(username, oldPassword, newPassword);
        userService.changePassword(changeLoginRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
