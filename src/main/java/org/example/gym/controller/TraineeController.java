package org.example.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Trainee-Controller")
@RestController
@RequestMapping("trainee")
@RequiredArgsConstructor
@Slf4j
@Validated
public class TraineeController {
    private final TraineeService traineeService;
    private final TraineeMapper mapper;

    /**
     * Registers a new trainee.
     *
     * @param requestDto The request DTO containing trainee registration details.
     * @return ResponseEntity with the registration response DTO.
     */
    @PostMapping(value = "/registration", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Register a new trainee", description = "Registers a new trainee in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Trainee registration successful", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RegistrationResponseDto> traineeRegistration(
            @RequestBody TraineeRegistrationRequestDto requestDto) {
        log.info("Controller: Trainee registration request received");

        TraineeEntity trainee = mapper.traineeRegistrationMapToEntity(requestDto);
        TraineeEntity traineeEntity = traineeService.createTraineeProfile(trainee);

        RegistrationResponseDto responseDto = mapper.traineeEntityMapToResponseDto(traineeEntity);
        log.info(" Controller: Trainee registration successful, Response: {}", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * Retrieves the profile of a trainee by username.
     *
     * @param username the username of the trainee
     * @return a response entity containing the trainee's profile details
     */
    @GetMapping("/{username}")
    @Operation(summary = "Get trainee profile", description = "Retrieves the profile of a trainee by their username.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "Trainee not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GetTraineeProfileResponseDto> getTraineeProfile(@PathVariable String username) {
        log.info("Controller: Get trainee profile request for username: {}", username);

        TraineeEntity trainee = traineeService.getTrainee(username);
        GetTraineeProfileResponseDto responseDto = mapper.traineeEntityMapToGetResponseTraineeDto(trainee);

        log.info("Controller: Get trainee profile request for username: {}", username);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Updates a trainee's profile.
     *
     * @param requestDto the updated details of the trainee
     * @return a response entity containing the updated trainee's response
     */
    @PutMapping
    @Operation(summary = "Update trainee profile", description = "Updates a trainee's profile information.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile updated successfully", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid update request"),
        @ApiResponse(responseCode = "404", description = "Trainee not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UpdateTraineeResponseDto> updateTraineeProfile(
            @RequestBody UpdateTraineeRequestDto requestDto) {
        log.info(" Controller: Update trainee profile request for username: {}", requestDto.getUsername());

        TraineeEntity traineeEntityUpdated = traineeService.updateTraineeProfile(requestDto);

        UpdateTraineeResponseDto responseDto = mapper.traineeEntityMapToUpdateResponse(traineeEntityUpdated);
        log.info("Controller: Trainee profile updated successfully, Response: {}", responseDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Deletes a trainee's profile by username.
     *
     * @param username the username of the trainee
     * @return a response entity with no content
     */
    @DeleteMapping("/{username}")
    @Operation(summary = "Delete trainee profile", description = "Deletes a trainee's profile by username.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile deleted successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "Trainee not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteTraineeProfile(@PathVariable String username) {
        log.info("Controller: Delete trainee profile request for username: {}", username);

        traineeService.deleteTraineeByUsername(username);
        log.info("Controller: Trainee profile deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Retrieves a list of unassigned active trainers for a trainee.
     *
     * @param traineeName the username of the trainee
     * @return a response entity containing the list of unassigned trainers
     */
    @GetMapping("/unassigned-trainers/{traineeName}")
    @Operation(summary = "Get unassigned trainers", description = "Retrieves a list of active trainers who"
            + " are not assigned to a trainee.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List retrieved successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "No trainers found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TrainerResponseDto>> getNotAssignedOnTraineeActiveTrainers(
            @PathVariable String traineeName) {
        log.info("Controller: Get not assigned on trainee active trainers request for username: {}", traineeName);

        List<TrainerEntity> unassignedTrainers = traineeService.getUnassignedTrainers(traineeName);
        List<TrainerResponseDto> responseDtos = mapper.mapToTrainerResponse(unassignedTrainers);

        log.info("Controller: Unassigned trainers retrieved successfully, Response size: {}", responseDtos.size());
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Updates the list of trainers assigned to a trainee.
     *
     * @param requestDto the details of the trainee and the trainers to assign
     * @return a response entity containing the updated list of trainers
     */
    @PutMapping("/trainers")
    @Operation(summary = "Update trainee's trainer list", description = "Updates the list of trainers "
            + "assigned to a trainee.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer list updated successfully", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid update request"),
        @ApiResponse(responseCode = "404", description = "Trainee or trainers not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TrainerResponseDto>> updateTraineeTrainerList(
            @RequestBody UpdateTraineeTrainerListRequestDto requestDto) {
        log.info(" Controller: Update trainee trainer list request for username: {}", requestDto.getTraineeUsername());

        TraineeEntity traineeEntity = traineeService.updateTraineeTrainerList(requestDto);

        List<TrainerResponseDto> responseDtos = mapper.updateTraineeTrainerListMapToTrainerResponse(traineeEntity);
        log.info(" Controller: Trainee trainer list updated successfully, Response size: {}", responseDtos.size());
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Activates a trainee's account.
     *
     * @param requestDto the details of the trainee to activate
     * @return a response entity with no content
     */
    @PatchMapping("/status")
    @Operation(summary = "Activate trainee account", description = "Activates a trainee's account.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainee account activated successfully", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid activation request"),
        @ApiResponse(responseCode = "404", description = "Trainee not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> toggleTraineeStatus(@RequestBody ActivateRequestDto requestDto) {
        log.info("Controller: toggle trainee profile request for username: {}",  requestDto.getUsername());

        traineeService.toggleTraineeStatus(requestDto);
        log.info(" Controller: Trainee profile toggle successfully");

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
