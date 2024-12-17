package org.example.gym.mapper;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.AddTrainingRequestDto;
import org.example.gym.dto.response.GetTrainerTrainingListResponseDto;
import org.example.gym.dto.response.TrainingResponseDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.springframework.stereotype.Component;


/**
 * Mapper class for converting TrainingEntity objects to their corresponding DTOs
 * and vice versa.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TrainingMapper {

    /**
     * Maps a list of TrainingEntity objects to a list of TrainingResponseDto.
     *
     * @param trainingsForTrainee the list of TrainingEntity objects to map
     * @return a list of TrainingResponseDto objects
     */
    public List<TrainingResponseDto> mapToDtoTrainingTrainee(List<TrainingEntity> trainingsForTrainee) {
        List<TrainingResponseDto> responseDtos = new ArrayList<>();
        for (TrainingEntity entity : trainingsForTrainee) {

            TrainingResponseDto responseDto = new TrainingResponseDto(
                    entity.getTrainingName(),
                    entity.getTrainingDate().toString(),
                    entity.getTrainingType().getTrainingTypeName(),
                    entity.getTrainingDuration(),
                    entity.getTrainer().getUser().getUsername());
            responseDtos.add(responseDto);
        }
        log.info("Mapped {} trainings for trainee to DTOs.", responseDtos.size());
        return responseDtos;
    }

    /**
     * Maps a list of TrainingEntity objects to a list of GetTrainerTrainingListResponseDto.
     *
     * @param trainingsForTrainer the list of TrainingEntity objects to map
     * @return a list of GetTrainerTrainingListResponseDto objects
     */
    public List<GetTrainerTrainingListResponseDto> mapToDtoTrainingTrainer(List<TrainingEntity> trainingsForTrainer) {
        List<GetTrainerTrainingListResponseDto> responseDtos = new ArrayList<>();
        for (TrainingEntity entity : trainingsForTrainer) {
            GetTrainerTrainingListResponseDto responseDto =
                    new GetTrainerTrainingListResponseDto(
                            entity.getTrainingName(),
                            entity.getTrainingDate().toString(),
                            entity.getTrainingType(),
                            entity.getTrainingDuration(),
                            entity.getTrainee().getUser().getUsername());
            responseDtos.add(responseDto);
        }
        log.info("Mapped {} trainings for trainer to DTOs.", responseDtos.size());
        return responseDtos;
    }

    /**
     * Maps an AddTrainingRequestDto to a TrainingEntity.
     *
     * @param requestDto the AddTrainingRequestDto to map
     * @return a TrainingEntity object
     */
    public TrainingEntity requestDtoMapToTrainingEntity(AddTrainingRequestDto requestDto,
                                                        TraineeEntity trainee,
                                                        TrainerEntity trainer) {
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTrainingDuration(requestDto.getTrainingDuration());
        trainingEntity.setTrainingDate(requestDto.getTrainingDate());
        trainingEntity.setTrainingName(requestDto.getTrainingName());
        trainingEntity.setTrainee(trainee);
        trainingEntity.setTrainer(trainer);

        trainingEntity.setTrainingType(trainer.getSpecialization());

        return trainingEntity;
    }
}
