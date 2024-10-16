package org.example.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.dto.request.TrainerRegistrationRequestDto;
import org.example.gym.dto.request.UpdateTrainerRequestDto;
import org.example.gym.dto.response.GetTrainerProfileResponseDto;
import org.example.gym.dto.response.RegistrationResponseDto;
import org.example.gym.dto.response.UpdateTrainerProfileResponseDto;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.mapper.TrainerMapper;
import org.example.gym.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling trainer-related operations.
 * Acts as a facade between the client and the TrainerService.
 */
@RestController
@RequestMapping("api/trainer")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class TrainerController {
    private final TrainerService trainerService;
    private final TrainerMapper mapper;

    /**
     * Registers a new trainer.
     *
     * @param requestDto The request DTO containing trainer registration details.
     * @return ResponseEntity with the registration response DTO.
     */
    @PostMapping("/registration")
    @Operation(summary = "Register a new trainer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Trainer registration successful", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid registration details"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RegistrationResponseDto> registerTrainer(
            @RequestBody TrainerRegistrationRequestDto requestDto) {
        log.info("Registering trainer: {} {}", requestDto.getFirstName(), requestDto.getLastName());

        TrainerEntity trainerEntity = mapper.trainerRegistrationMapToEntity(requestDto);
        TrainerEntity savedTrainer = trainerService.createTrainerProfile(trainerEntity);

        RegistrationResponseDto responseDto = mapper.trainerMapToResponse(savedTrainer);
        log.info("Controller: Trainer registration successful, Response: {}", responseDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * Retrieves a trainer's profile by username.
     *
     * @param username The username of the trainer.
     * @return ResponseEntity with the trainer's profile response DTO.
     */
    @GetMapping
    @Operation(summary = "Get a trainer's profile by username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer profile retrieved successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "Trainer not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GetTrainerProfileResponseDto> getTrainerProfile(@RequestHeader String username) {
        log.info("Fetching profile for trainer: {}", username);

        TrainerEntity trainer = trainerService.getTrainer(username);
        GetTrainerProfileResponseDto responseDto = mapper.trainerEntityMapToGetResponse(trainer);
        log.info("Controller: Trainer profile retrieved successfully, Response: {}", responseDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Updates the trainer's profile.
     *
     * @param requestDto The request DTO containing the updated trainer details.
     * @return ResponseEntity with the updated trainer profile response DTO.
     */
    @PutMapping("/update")
    @Operation(summary = "Update trainer profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer profile updated successfully", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid update details"),
        @ApiResponse(responseCode = "404", description = "Trainer not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UpdateTrainerProfileResponseDto> updateTrainerProfile(
            @RequestBody UpdateTrainerRequestDto requestDto) {
        log.info("Updating trainer profile for: {}", requestDto.getUsername());

        TrainerEntity updatedTrainer = mapper.updateRequestDtoMapToTrainerEntity(requestDto);
        TrainerEntity savedTrainer = trainerService.updateTrainerProfile(updatedTrainer);

        UpdateTrainerProfileResponseDto responseDto = mapper.updateTrainerProfileMapToResponseDto(savedTrainer);
        log.info("Controller: Trainer profile updated successfully, Response: {}", responseDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Activates a trainer's account.
     *
     * @param requestDto The request DTO containing the username to activate.
     * @return ResponseEntity indicating the result of the activation.
     */
    @PatchMapping("/toggle-activate")
    @Operation(summary = "Activate a trainer's account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer activated successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "Trainer not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> toggleTrainerStatus(@RequestBody ActivateRequestDto requestDto) {
        log.info("Controller: toggle trainer profile request for username: {}", requestDto.getUsername());

        trainerService.toggleTrainerStatus(requestDto);

        log.info("Controller: Trainer profile toggle successfully");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}



