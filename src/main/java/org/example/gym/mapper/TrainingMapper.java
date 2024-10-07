package org.example.gym.mapper;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.paylod.request.AddTrainingRequestDto;
import org.example.gym.paylod.response.GetTrainerTrainingListResponseDto;
import org.example.gym.paylod.response.TrainingResponseDto;
import org.example.gym.service.TraineeService;
import org.example.gym.service.TrainerService;
import org.example.gym.service.TrainingService;
import org.example.gym.service.TrainingTypeService;
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
