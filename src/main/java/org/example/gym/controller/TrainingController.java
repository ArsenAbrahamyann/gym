package org.example.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.AddTrainingRequestDto;
import org.example.gym.dto.request.TraineeTrainingsRequestDto;
import org.example.gym.dto.request.TrainerTrainingRequestDto;
import org.example.gym.dto.response.GetTrainerTrainingListResponseDto;
import org.example.gym.dto.response.TrainingResponseDto;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.mapper.TrainingMapper;
import org.example.gym.service.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling training-related operations.
 * Acts as a facade between the client and the TrainingService.
 */
@Tag(name = "Training-Controller")
@RestController
@RequestMapping("training")
@RequiredArgsConstructor
@Slf4j
public class TrainingController {
    private final TrainingService trainingService;
    private final TrainingMapper mapper;

    /**
     * Fetches the list of trainings for a specific trainee.
     *
     * @param traineeName the name of the trainee
     * @param periodFrom  optional start period for filtering trainings
     * @param periodTo    optional end period for filtering trainings
     * @param trainerName optional trainer's name for filtering
     * @param trainingType optional training type for filtering
     * @return a list of training response DTOs for the trainee
     */
    @GetMapping("/trainee")
    @Operation(summary = "Get trainings for a trainee", description = "Fetches the list of trainings"
            + " for a specific trainee with optional filters.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainings retrieved successfully",
                content = @Content(schema = @Schema(implementation = TrainingResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "404", description = "Trainee not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TrainingResponseDto>> getTraineeTrainingsList(
            @RequestParam String traineeName,
            @RequestParam(required = false) LocalDateTime periodFrom,
            @RequestParam(required = false) LocalDateTime periodTo,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingType) {

        log.info("Fetching training list for trainee: {}", traineeName);
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto(traineeName, periodFrom,
                periodTo, trainerName, trainingType);


        List<TrainingEntity> trainingsForTrainee = trainingService.getTrainingsForTrainee(
                requestDto);

        List<TrainingResponseDto> responseDtos = mapper.mapToDtoTrainingTrainee(trainingsForTrainee);
        log.info("Response: {} trainings retrieved", responseDtos.size());
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Fetches the list of trainings for a specific trainer.
     *
     * @param trainerName the name of the trainer
     * @param periodFrom  optional start period for filtering trainings
     * @param periodTo    optional end period for filtering trainings
     * @param traineeName optional trainee's name for filtering
     * @return a list of training response DTOs for the trainer
     */
    @GetMapping("/trainer")
    @Operation(summary = "Get trainings for a trainer", description = "Fetches the list of trainings for "
            + "a specific trainer with optional filters.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainings retrieved successfully",
                content = @Content(schema = @Schema(implementation = GetTrainerTrainingListResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "404", description = "Trainer not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<GetTrainerTrainingListResponseDto>> getTrainerTrainingList(
            @RequestParam String trainerName,
            @RequestParam(required = false) LocalDateTime periodFrom,
            @RequestParam(required = false) LocalDateTime periodTo,
            @RequestParam(required = false) String traineeName) {

        log.info("Fetching training list for trainer: {}", trainerName);

        TrainerTrainingRequestDto requestDto = new TrainerTrainingRequestDto(trainerName,
                periodFrom, periodTo, traineeName);
        List<TrainingEntity> trainingsForTrainer = trainingService.getTrainingsForTrainer(requestDto);

        List<GetTrainerTrainingListResponseDto> responseDto = mapper.mapToDtoTrainingTrainer(trainingsForTrainer);
        log.info("Response: {} trainings retrieved", responseDto.size());
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Adds a new training session.
     *
     * @param requestDto the request DTO containing training details
     * @return a response entity indicating the result of the operation
     */
    @PostMapping("/registration")
    @Operation(summary = "Add a new training", description = "Adds a new training session to the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Training added successfully", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid training details"),
        @ApiResponse(responseCode = "404", description = "Trainee or Trainer not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> addTraining(@RequestBody AddTrainingRequestDto requestDto) {
        log.debug("Received training date: {}", requestDto.getTrainingDate());
        trainingService.addTraining(requestDto);
        log.info("Training added successfully");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Deletes a training session specified by the ID.
     * It logs the action of deleting the training with the given ID and then calls the
     * service to perform the deletion.
     *
     * @param trainingId The ID of the training session to be deleted.
     * @return {@link ResponseEntity} indicating the outcome (success or failure).
     */
    @DeleteMapping("/{trainingId}")
    @Operation(summary = "Delete a training", description = "Deletes a training session by ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Training deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Training not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteTraining(@PathVariable Long trainingId) {
        log.info("Deleting training with ID: {}", trainingId);
        trainingService.deleteTraining(trainingId);

        log.info("Training with ID {} deleted successfully", trainingId);
        return ResponseEntity.ok().build();
    }
}
