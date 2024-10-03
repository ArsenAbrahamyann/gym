package org.example.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.paylod.request.TrainerRegistrationRequestDto;
import org.example.paylod.request.UpdateTrainerRequestDto;
import org.example.paylod.response.GetTrainerProfileResponseDto;
import org.example.paylod.response.RegistrationResponseDto;
import org.example.paylod.response.TraineeListResponseDto;
import org.example.paylod.response.UpdateTrainerProfileResponseDto;
import org.example.service.TrainerService;
import org.example.service.TrainingTypeService;
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
}
