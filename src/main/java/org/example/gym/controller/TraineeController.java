package org.example.gym.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.dto.request.TraineeRegistrationRequestDto;
import org.example.gym.dto.request.UpdateTraineeRequestDto;
import org.example.gym.dto.request.UpdateTraineeTrainerListRequestDto;
import org.example.gym.dto.response.GetTraineeProfileResponseDto;
import org.example.gym.dto.response.RegistrationResponseDto;
import org.example.gym.dto.response.TrainerResponseDto;
import org.example.gym.dto.response.UpdateTraineeResponseDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.mapper.TraineeMapper;
import org.example.gym.service.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing trainee-related operations.
 */
@RestController
@RequestMapping("api/trainee")
@RequiredArgsConstructor
@Slf4j
@Validated
public class TraineeController {
    private final TraineeService traineeService;
    private final TraineeMapper mapper;

    /**
     * Registers a new trainee.
     *
     * @param requestDto the registration details of the trainee
     * @return a response entity containing the registration response
     */
    @PostMapping(value = "/registration", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RegistrationResponseDto> traineeRegistration(
            @RequestBody TraineeRegistrationRequestDto requestDto) {
        log.info("Controller: trainee registration");

        TraineeEntity trainee = mapper.traineeRegistrationMapToEntity(requestDto);
        TraineeEntity traineeEntity = traineeService.createTraineeProfile(trainee);

        RegistrationResponseDto responseDto = mapper.traineeEntityMapToResponseDto(traineeEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * Retrieves the profile of a trainee by username.
     *
     * @param username the username of the trainee
     * @return a response entity containing the trainee's profile details
     */
    @GetMapping("/{username}")
    public ResponseEntity<GetTraineeProfileResponseDto> getTraineeProfile(@PathVariable String username) {
        log.info("Controller: Get trainee profile for username: {}", username);

        TraineeEntity trainee = traineeService.getTrainee(username);
        GetTraineeProfileResponseDto responseDto = mapper.traineeEntityMapToGetResponseTraineeDto(trainee);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * Updates a trainee's profile.
     *
     * @param requestDto the updated details of the trainee
     * @return a response entity containing the updated trainee's response
     */
    @PutMapping("/update")
    public ResponseEntity<UpdateTraineeResponseDto> updateTraineeProfile(
            @RequestBody UpdateTraineeRequestDto requestDto) {
        log.info("Controller: Update trainee profile for username: {}", requestDto.getUsername());

        TraineeEntity trainee = mapper.updateDtoMapToTraineeEntity(requestDto);
        TraineeEntity traineeEntityUpdated = traineeService.updateTraineeProfile(trainee);

        UpdateTraineeResponseDto responseDto = mapper.traineeEntityMapToUpdateResponse(traineeEntityUpdated);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Deletes a trainee's profile by username.
     *
     * @param username the username of the trainee
     * @return a response entity with no content
     */
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> deleteTraineeProfile(@PathVariable String username) {
        log.info("Controller: Delete trainee profile for username: {}", username);

        traineeService.deleteTraineeByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Retrieves a list of unassigned active trainers for a trainee.
     *
     * @param traineeUsername the username of the trainee
     * @return a response entity containing the list of unassigned trainers
     */
    @GetMapping("/unassigned-trainers/{traineeUsername}")
    public ResponseEntity<List<TrainerResponseDto>> getNotAssignedOnTraineeActiveTrainers(
            @PathVariable String traineeUsername) {
        log.info("Controller: Get not assigned on trainee active trainers for username: {}", traineeUsername);

        List<TrainerEntity> unassignedTrainers = traineeService.getUnassignedTrainers(traineeUsername);
        List<TrainerResponseDto> responseDtos = mapper.mapToTrainerResponse(unassignedTrainers);

        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Updates the list of trainers assigned to a trainee.
     *
     * @param requestDto the details of the trainee and the trainers to assign
     * @return a response entity containing the updated list of trainers
     */
    @PutMapping("/update/trainerList")
    public ResponseEntity<List<TrainerResponseDto>> updateTraineeTrainerList(
            @RequestBody UpdateTraineeTrainerListRequestDto requestDto) {
        log.info("Controller: Update Trainee's Trainer list for username: {}", requestDto.getTraineeUsername());

        TraineeEntity trainee = mapper.updateTraineeTrainerListMapToEntity(requestDto);
        TraineeEntity traineeEntity = traineeService.updateTraineeTrainers(trainee);

        List<TrainerResponseDto> responseDtos = mapper.updateTraineeTrainerListMapToTrainerResponse(traineeEntity);
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Activates a trainee's account.
     *
     * @param requestDto the details of the trainee to activate
     * @return a response entity with no content
     */
    @PatchMapping("/activate")
    public ResponseEntity<Void> activateTrainee(@RequestBody ActivateRequestDto requestDto) {
        log.info("Controller: Activate trainee with username: {}", requestDto.getUsername());

        traineeService.toggleTraineeStatus(requestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Deactivates a trainee's account.
     *
     * @param requestDto the details of the trainee to deactivate
     * @return a response entity with no content
     */
    @PatchMapping("/de-activate")
    public ResponseEntity<Void> deActivateTrainee(@RequestBody ActivateRequestDto requestDto) {
        log.info("Controller: DeActivate trainee with username: {}", requestDto.getUsername());

        traineeService.toggleTraineeStatus(requestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
