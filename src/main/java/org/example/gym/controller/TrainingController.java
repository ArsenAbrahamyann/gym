package org.example.gym.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.mapper.TrainingMapper;
import org.example.gym.paylod.request.AddTrainingRequestDto;
import org.example.gym.paylod.request.TraineeTrainingsRequestDto;
import org.example.gym.paylod.request.TrainerTrainingRequestDto;
import org.example.gym.paylod.response.GetTrainerTrainingListResponseDto;
import org.example.gym.paylod.response.TrainingResponseDto;
import org.example.gym.service.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
            @RequestBody TraineeTrainingsRequestDto requestDto) {

        log.info("Fetching training list for trainee: {}", requestDto.getTraineeName());


        List<TrainingEntity> trainingsForTrainee = trainingService.getTrainingsForTrainee(
                requestDto);

        List<TrainingResponseDto> responseDtos = mapper.mapToDtoTrainingTrainee(trainingsForTrainee);
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/trainer")
    public ResponseEntity<List<GetTrainerTrainingListResponseDto>> getTrainerTrainingList(
           @RequestBody TrainerTrainingRequestDto requestDto) {

        log.info("Fetching training list for trainer: {}", requestDto.getTrainerUsername());

        List<TrainingEntity> trainingsForTrainer = trainingService.getTrainingsForTrainer(requestDto);

        List<GetTrainerTrainingListResponseDto> responseDto = mapper.mapToDtoTrainingTrainer(trainingsForTrainer);
        return ResponseEntity.ok(responseDto);
    }

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
