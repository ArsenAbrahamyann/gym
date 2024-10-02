package org.example.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.paylod.request.GetNotAssignedOnTraineeActiveTrainersRequestDto;
import org.example.paylod.request.IsActiveRequestDto;
import org.example.paylod.request.TraineeRegistrationRequestDto;
import org.example.paylod.request.UpdateTraineeRequestDto;
import org.example.paylod.request.UpdateTraineeTrainerListRequestDto;
import org.example.paylod.request.UsernameRequestDto;
import org.example.paylod.response.GetTraineeProfileResponseDto;
import org.example.paylod.response.RegistrationResponseDto;
import org.example.paylod.response.TrainerResponseDto;
import org.example.paylod.response.UpdateTraineeResponseDto;
import org.example.service.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/trainee")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class TraineeController {
    private final TraineeService traineeService;

    @PostMapping (value = "/registration", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> traineeRegistration(@RequestPart(required = false) String dateOfBrith,
                                                 @RequestPart(required = false) String address,
                                                 @RequestPart String firsName,
                                                 @RequestPart String lastName) {
        log.info("Controller: trainee registration");
        TraineeRegistrationRequestDto traineeRegistrationRequestDto = new TraineeRegistrationRequestDto(firsName,
                lastName, dateOfBrith, address);
        RegistrationResponseDto traineeProfile = traineeService.createTraineeProfile(traineeRegistrationRequestDto);
        return new ResponseEntity<>(traineeProfile, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getTraineeProfile (@RequestPart String username) {
        log.info("Controller: Get trainee profile.");
        UsernameRequestDto usernameRequestDto = new UsernameRequestDto(username);
        GetTraineeProfileResponseDto trainee = traineeService.getTrainee(usernameRequestDto.getUsername());
        return new ResponseEntity<>(trainee, HttpStatus.OK);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateTraineeProfile(@RequestPart String username,
                                                  @RequestPart String firstName,
                                                  @RequestPart String lastName,
                                                  @RequestPart(required = false) String dateOfBirth,
                                                  @RequestPart(required = false) String address,
                                                  @RequestParam boolean isPublic) {
        log.info("Controller: Update trainee profile");
        UpdateTraineeRequestDto updateTraineeRequestDto = new UpdateTraineeRequestDto(username, firstName, lastName,
                dateOfBirth, address, isPublic);
        //...
        UpdateTraineeResponseDto responseDto = new UpdateTraineeResponseDto();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteTraineeProfile(@RequestPart String username) {
        log.info("Controller: Delete trainee profile.");
        UsernameRequestDto usernameRequestDto = new UsernameRequestDto(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getNotAssignedOnTraineeActiveTrainers(@RequestPart String traineeUsername) {
        log.info("Controller: Get not assigned on trainee active trainers.");
        GetNotAssignedOnTraineeActiveTrainersRequestDto assigned =
                new GetNotAssignedOnTraineeActiveTrainersRequestDto(traineeUsername);
        //...
        TrainerResponseDto responseDto = new TrainerResponseDto();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value = "/update/Trainee/trainerList")
    public ResponseEntity<?> updateTraineeTrainerList(@RequestPart String traineeUsername,
                                                      @RequestParam(name = "TrainersIdList") List<Long> trainerIdList,
                                                      @RequestPart String trainerUsername) {
        log.info("Controller: Updatem Trainee is Trainer list");
        UpdateTraineeTrainerListRequestDto updateTraineeTrainerListRequestDto = new UpdateTraineeTrainerListRequestDto(traineeUsername,
                trainerIdList, trainerUsername);
        //...
        TrainerResponseDto responseDto = new TrainerResponseDto();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }



    @PatchMapping(value = "/activate")
    public ResponseEntity<?> activateTrainee(@RequestPart String username,
                                             @RequestParam boolean isActive) {
        log.info("Controller: Activate trainee.");
        IsActiveRequestDto isActiveRequestDto = new IsActiveRequestDto(username, isActive);
        //sarqel responsedto
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/de-activate")
    public ResponseEntity<?> deActivateTrainee(@RequestPart String username,
                                             @RequestParam boolean isActive) {
        log.info("Controller: DeActivate trainee.");
        IsActiveRequestDto activeDto = new IsActiveRequestDto(username, isActive);
        //sarqel responsedto
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
