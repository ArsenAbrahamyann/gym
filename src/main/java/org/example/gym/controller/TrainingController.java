package org.example.gym.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.mapper.TrainingMapper;
import org.example.gym.paylod.request.AddTrainingRequestDto;
import org.example.gym.paylod.response.GetTrainerTrainingListResponseDto;
import org.example.gym.paylod.response.TrainingResponseDto;
import org.example.gym.service.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/trainee")
    public ResponseEntity<List<TrainingResponseDto>> getTraineeTrainingsList(
            @RequestParam String traineeName,
            @RequestParam(required = false) LocalDateTime periodFrom,
            @RequestParam(required = false) LocalDateTime periodTo,
            @RequestParam(required = false) String trainingName,
            @RequestParam(required = false) String trainingType) {

        log.info("Fetching training list for trainee: {}", traineeName);

        List<TrainingEntity> trainingsForTrainee = trainingService.getTrainingsForTrainee(
                traineeName, periodFrom, periodTo, trainingName, trainingType);

        List<TrainingResponseDto> responseDtos = mapper.mapToDtoTrainingTrainee(trainingsForTrainee);
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/trainer")
    public ResponseEntity<List<GetTrainerTrainingListResponseDto>> getTrainerTrainingList(
            @RequestParam String trainerUsername,
            @RequestParam(required = false) LocalDateTime periodFrom,
            @RequestParam(required = false) LocalDateTime periodTo,
            @RequestParam(required = false) String traineeName) {

        log.info("Fetching training list for trainer: {}", trainerUsername);

        List<TrainingEntity> trainingsForTrainer = trainingService.getTrainingsForTrainer(
                trainerUsername, periodFrom, periodTo, traineeName);

        List<GetTrainerTrainingListResponseDto> responseDto = mapper.mapToDtoTrainingTrainer(trainingsForTrainer);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addTraining(
            @RequestParam String traineeUsername,
            @RequestParam String trainerUsername,
            @RequestParam String trainingName,
            @RequestParam LocalDateTime trainingDate,
            @RequestParam Integer trainingDuration) {

        log.info("Adding training for trainee: {} with trainer: {}", traineeUsername, trainerUsername);

        AddTrainingRequestDto requestDto = new AddTrainingRequestDto(
                traineeUsername, trainerUsername, trainingName, trainingDate, trainingDuration);

        TrainingEntity trainingEntity = mapper.requestDtoMapToTrainingEntity(requestDto);
        trainingService.addTraining(trainingEntity);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
