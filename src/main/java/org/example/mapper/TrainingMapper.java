package org.example.mapper;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.paylod.request.AddTrainingRequestDto;
import org.example.paylod.response.GetTrainerTrainingListResponseDto;
import org.example.paylod.response.TrainingResponseDto;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.example.service.TrainingTypeService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingMapper {
    private final TrainingService trainingService;
    private final TrainingTypeService typeService;
    private final TrainerService trainerService;
    private final TraineeService traineeService;

    public List<TrainingResponseDto> mapToDtoTrainingTrainee(List<TrainingEntity> trainingsForTrainee) {
        List<TrainingResponseDto> responseDtos = new ArrayList<>();
        for (TrainingEntity entity : trainingsForTrainee) {
            TrainingResponseDto responseDto = new TrainingResponseDto(entity.getTrainingName(),
                    entity.getTrainingDate(), entity.getTrainingType(), entity.getTrainingDuration(),
                    entity.getTrainer().getUsername());
            responseDtos.add(responseDto);
        }
        return responseDtos;
    }

    public List<GetTrainerTrainingListResponseDto> mapToDtoTrainingTrainer(List<TrainingEntity> trainingsForTrainer) {
        List<GetTrainerTrainingListResponseDto> responseDtos = new ArrayList<>();
        for (TrainingEntity entity : trainingsForTrainer) {
            GetTrainerTrainingListResponseDto responseDto =
                    new GetTrainerTrainingListResponseDto(entity.getTrainingName(),
                    entity.getTrainingDate(), entity.getTrainingType(), entity.getTrainingDuration(),
                    entity.getTrainee().getUsername());
            responseDtos.add(responseDto);
        }
        return responseDtos;
    }

    public TrainingEntity requestDtoMapToTrainingEntity(AddTrainingRequestDto requestDto) {
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTrainingDuration(requestDto.getTrainingDuration());
        trainingEntity.setTrainingDate(requestDto.getTrainingDate());
        trainingEntity.setTrainingName(requestDto.getTrainingName());
        TraineeEntity trainee = traineeService.getTrainee(requestDto.getTraineeUsername());
        trainingEntity.setTrainee(trainee);
        TrainerEntity trainer = trainerService.getTrainer(requestDto.getTrainerUsername());
        trainingEntity.setTrainer(trainer);
        return trainingEntity;
    }
}
