package org.example.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.paylod.request.GetTraineeTrainingsListRequestDto;
import org.example.paylod.request.GetTrainerTrainingListRequestDto;
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

    @GetMapping(value = "/trainee")
    public ResponseEntity<?> getTraineeTrainingsList(@RequestPart String traineeName,
                                                     @RequestPart(required = false) String periodFrom,
                                                     @RequestPart(required = false) String periodTo,
                                                     @RequestPart(required = false) String trainingName,
                                                     @RequestPart(required = false) String trainingType) {
        log.info("Controller: Get trainee trainings list.");
        GetTraineeTrainingsListRequestDto listDto = new GetTraineeTrainingsListRequestDto(traineeName, periodFrom,
                periodTo, trainingName, trainingType);
        //...
        TrainingResponseDto responseDto = new TrainingResponseDto();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/trainer")
    public ResponseEntity<?> getTrainerTrainingList(@RequestPart String trainerUsername,
                                                    @RequestPart(required = false) String periodFrom,
                                                    @RequestPart(required = false) String periodTo,
                                                    @RequestPart(required = false) String traineeName) {
        log.info("Controller: Get trainer trainings List.");
        GetTrainerTrainingListRequestDto listDto = new GetTrainerTrainingListRequestDto(trainerUsername, periodFrom, periodTo,
                traineeName);
        //...
        GetTrainerTrainingListResponseDto responseDto = new GetTrainerTrainingListResponseDto();
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
                                         @RequestPart String trainingDate,
                                         @RequestPart String trainingDuration) {
        log.info("Controller: Add training.");
        //sarqel requestdto
        // respons petq chi
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
