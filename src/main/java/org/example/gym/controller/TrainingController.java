package org.example.gym.controller;

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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling training-related operations.
 * Acts as a facade between the client and the TrainingService.
 */
@RestController
@RequestMapping("api/training")
@CrossOrigin
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
    public ResponseEntity<List<TrainingResponseDto>> getTraineeTrainingsList(
            @RequestHeader String traineeName,
            @RequestHeader(required = false) LocalDateTime periodFrom,
            @RequestHeader(required = false) LocalDateTime periodTo,
            @RequestHeader(required = false) String trainerName,
            @RequestHeader(required = false) String trainingType) {

        log.info("Fetching training list for trainee: {}", traineeName);
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto(traineeName, periodFrom,
                periodTo, trainerName, trainingType);


        List<TrainingEntity> trainingsForTrainee = trainingService.getTrainingsForTrainee(
                requestDto);

        List<TrainingResponseDto> responseDtos = mapper.mapToDtoTrainingTrainee(trainingsForTrainee);
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
    public ResponseEntity<List<GetTrainerTrainingListResponseDto>> getTrainerTrainingList(
            @RequestHeader String trainerName,
            @RequestHeader(required = false) String periodFrom,
            @RequestHeader(required = false) String periodTo,
            @RequestHeader(required = false) String traineeName) {

        log.info("Fetching training list for trainer: {}", trainerName);

        TrainerTrainingRequestDto requestDto = new TrainerTrainingRequestDto(trainerName,
                LocalDateTime.parse(periodFrom), LocalDateTime.parse(periodTo), traineeName);
        List<TrainingEntity> trainingsForTrainer = trainingService.getTrainingsForTrainer(requestDto);

        List<GetTrainerTrainingListResponseDto> responseDto = mapper.mapToDtoTrainingTrainer(trainingsForTrainer);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Adds a new training session.
     *
     * @param requestDto the request DTO containing training details
     * @return a response entity indicating the result of the operation
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addTraining(
            @RequestBody AddTrainingRequestDto requestDto) {

        log.info("Adding training for trainee: {} with trainer: {}", requestDto.getTraineeUsername(),
                requestDto.getTrainerUsername());

        TrainingEntity trainingEntity = mapper.requestDtoMapToTrainingEntity(requestDto);
        trainingService.addTraining(trainingEntity);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
