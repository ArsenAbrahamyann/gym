package org.example.gym.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.paylod.request.TrainerRegistrationRequestDto;
import org.example.gym.paylod.request.UpdateTrainerRequestDto;
import org.example.gym.paylod.response.GetTrainerProfileResponseDto;
import org.example.gym.paylod.response.RegistrationResponseDto;
import org.example.gym.paylod.response.TraineeListResponseDto;
import org.example.gym.paylod.response.UpdateTrainerProfileResponseDto;
import org.example.gym.service.TrainerService;
import org.example.gym.service.TrainingTypeService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainerMapper {
    private final TrainingTypeService trainingTypeService;
    private final TrainerService trainerService;

    public TrainerEntity trainerRegistrationMapToEntity(TrainerRegistrationRequestDto registrationDto) {
      TrainerEntity trainer = new TrainerEntity();
        TrainingTypeEntity trainingType = trainingTypeService.findById(registrationDto.getTrainingTypeId());
        trainer.setSpecialization(trainingType);
        trainer.setFirstName(registrationDto.getFirstName());
        trainer.setLastName(registrationDto.getLastName());
        return trainer;
    }

    public RegistrationResponseDto TrainerMapToEntity(TrainerEntity trainerProfile) {
        RegistrationResponseDto responseDto = new RegistrationResponseDto(trainerProfile.getUsername(),
                trainerProfile.getPassword());
        return responseDto;
    }

    public GetTrainerProfileResponseDto trainerEntityMapToGetResponse(TrainerEntity trainer) {

        Set<TraineeEntity> trainees = trainer.getTrainees();
        List<TraineeListResponseDto> traineeList = new ArrayList<>();
        for (TraineeEntity entity : trainees) {
            TraineeListResponseDto responseDto = new TraineeListResponseDto(
                    entity.getUsername(), entity.getLastName(), entity.getFirstName());
            traineeList.add(responseDto);
        }
        GetTrainerProfileResponseDto responseDto = new GetTrainerProfileResponseDto(trainer.getFirstName(),
                trainer.getLastName(), trainer.getSpecialization().getId(), trainer.getIsActive(),
               traineeList);
        return responseDto;
    }

    public TrainerEntity UpdateRequestDtoMapToTrainerEntity(UpdateTrainerRequestDto updateTrainerRequestDto) {
        TrainerEntity trainer = trainerService.getTrainer(
                updateTrainerRequestDto.getUsername());
        trainer.setFirstName(updateTrainerRequestDto.getFirstName());
        trainer.setLastName(updateTrainerRequestDto.getLastName());
        trainer.setUsername(updateTrainerRequestDto.getUsername());
        TrainingTypeEntity trainingType = trainingTypeService.findById(updateTrainerRequestDto.getTrainingTypeId());
        trainer.setSpecialization(trainingType);
        trainer.setIsActive(updateTrainerRequestDto.isActive());
        return trainer;
    }

    public UpdateTrainerProfileResponseDto updateTrainerProfileMapToResponseDto(TrainerEntity entity) {

        Set<TraineeEntity> trainees = entity.getTrainees();
        List<TraineeListResponseDto> responseDtos = new ArrayList<>();
        for (TraineeEntity trainee: trainees) {
            TraineeListResponseDto responseDto = new TraineeListResponseDto(trainee.getUsername(),
                    trainee.getLastName(), trainee.getFirstName());
            responseDtos.add(responseDto);
        }
        UpdateTrainerProfileResponseDto responseDto = new UpdateTrainerProfileResponseDto(entity.getUsername(),
                entity.getFirstName(), entity.getLastName(), entity.getSpecialization().getId(),
                entity.getIsActive(), responseDtos);
        return responseDto;
    }

    public TrainerEntity updateRequestDtoMapToTrainerEntity(UpdateTrainerRequestDto updateDto) {
        TrainerEntity trainer = trainerService.getTrainer(updateDto.getUsername());
        trainer.setIsActive(updateDto.isActive());
        trainer.setFirstName(updateDto.getFirstName());
        trainer.setLastName(updateDto.getLastName());
        trainer.setUsername(updateDto.getUsername());
        TrainingTypeEntity trainingType = trainingTypeService.findById(updateDto.getTrainingTypeId());
        trainer.setSpecialization(trainingType);
        return trainer;
    }

    public RegistrationResponseDto trainerMapToResponse(TrainerEntity savedTrainer) {
        RegistrationResponseDto responseDto = new RegistrationResponseDto(savedTrainer.getUsername(),
                savedTrainer.getPassword());
        return responseDto;
    }
}
