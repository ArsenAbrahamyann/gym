package org.example.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingEntity;
import org.example.mapper.TrainingMapper;
import org.example.paylod.request.AddTrainingRequestDto;
import org.example.paylod.response.TrainingResponseDto;
import org.example.paylod.response.GetTrainerTrainingListResponseDto;
import org.example.paylod.response.TrainingTypesResponseDto;
import org.example.service.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
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

    @GetMapping(value = "/trainee")
    public ResponseEntity<?> getTraineeTrainingsList(@RequestPart String traineeName,
                                                     @RequestPart(required = false) LocalDateTime periodFrom,
                                                     @RequestPart(required = false) LocalDateTime periodTo,
                                                     @RequestPart(required = false) String trainingName,
                                                     @RequestPart(required = false) String trainingType) {
        log.info("Controller: Get trainee trainings list.");

        List<TrainingEntity> trainingsForTrainee = trainingService.getTrainingsForTrainee(traineeName, periodFrom,
                periodTo, trainingName, trainingType);
        List<TrainingResponseDto> responseDtos = mapper.mapToDtoTrainingTrainee(trainingsForTrainee);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/trainer")
    public ResponseEntity<?> getTrainerTrainingList(@RequestPart String trainerUsername,
                                                    @RequestPart(required = false) LocalDateTime periodFrom,
                                                    @RequestPart(required = false) LocalDateTime periodTo,
                                                    @RequestPart(required = false) String traineeName) {
        log.info("Controller: Get trainer trainings List.");
        List<TrainingEntity> trainingsForTrainer = trainingService.getTrainingsForTrainer(trainerUsername, periodFrom,
                periodTo, traineeName);
        List<GetTrainerTrainingListResponseDto> responseDto = mapper.mapToDtoTrainingTrainer(
                trainingsForTrainer);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/types")
    public ResponseEntity<?> getTrainingType() {
        log.info("Controller: Get training types");
        //sarqel responsedto training types,type,typeid
        List<TrainingTypesResponseDto> responseDtos = new ArrayList<>();
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }





    @PostMapping(value = "/add")
    public ResponseEntity<?> addTraining(@RequestPart String traineeUsername,
                                         @RequestPart String trainerUsername,
                                         @RequestPart String trainingName,
                                         @RequestPart LocalDateTime trainingDate,
                                         @RequestPart Integer trainingDuration) {
        log.info("Controller: Add training.");
        AddTrainingRequestDto requestDto = new AddTrainingRequestDto(traineeUsername, trainerUsername, trainingName,
                trainingDate, trainingDuration);
        TrainingEntity trainingEntity = mapper.requestDtoMapToTrainingEntity(requestDto);
        trainingService.addTraining(trainingEntity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
