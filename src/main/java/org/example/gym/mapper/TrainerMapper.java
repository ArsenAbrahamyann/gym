package org.example.gym.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.TrainerRegistrationRequestDto;
import org.example.gym.dto.response.GetTrainerProfileResponseDto;
import org.example.gym.dto.response.RegistrationResponseDto;
import org.example.gym.dto.response.TraineeListResponseDto;
import org.example.gym.dto.response.UpdateTrainerProfileResponseDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for Trainer-related DTOs and entities.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TrainerMapper {

    /**
     * Maps a TrainerRegistrationRequestDto to a TrainerEntity.
     *
     * @param registrationDto the registration request DTO
     * @return the mapped TrainerEntity
     */
    public TrainerEntity trainerRegistrationMapToEntity(TrainerRegistrationRequestDto registrationDto) {
        log.debug("Mapping TrainerRegistrationRequestDto to TrainerEntity: {}", registrationDto);
        TrainerEntity trainer = new TrainerEntity();
        TrainingTypeEntity trainingType = new TrainingTypeEntity();
        trainingType.setId(registrationDto.getTrainingTypeId());
        trainer.setSpecialization(trainingType);
        trainer.setIsActive(true);
        trainer.setFirstName(registrationDto.getFirstName());
        trainer.setLastName(registrationDto.getLastName());
        log.info("Mapped TrainerEntity: {}", trainer);
        return trainer;
    }

    /**
     * Maps a TrainerEntity to a GetTrainerProfileResponseDto.
     *
     * @param trainer the TrainerEntity to map
     * @return the mapped GetTrainerProfileResponseDto
     */
    public GetTrainerProfileResponseDto trainerEntityMapToGetResponse(TrainerEntity trainer) {
        log.debug("Mapping TrainerEntity to GetTrainerProfileResponseDto: {}", trainer);
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
        log.info("Mapped GetTrainerProfileResponseDto: {}", responseDto);
        return responseDto;
    }


    /**
     * Maps a TrainerEntity to an UpdateTrainerProfileResponseDto.
     *
     * @param entity the TrainerEntity to map
     * @return the mapped UpdateTrainerProfileResponseDto
     */
    public UpdateTrainerProfileResponseDto updateTrainerProfileMapToResponseDto(TrainerEntity entity) {
        log.debug("Mapping TrainerEntity to UpdateTrainerProfileResponseDto: {}", entity);
        Set<TraineeEntity> trainees = entity.getTrainees();
        List<TraineeListResponseDto> responseDtos = new ArrayList<>();
        for (TraineeEntity trainee : trainees) {
            TraineeListResponseDto responseDto = new TraineeListResponseDto(trainee.getUsername(),
                    trainee.getLastName(), trainee.getFirstName());
            responseDtos.add(responseDto);
        }
        UpdateTrainerProfileResponseDto responseDto = new UpdateTrainerProfileResponseDto(entity.getUsername(),
                entity.getFirstName(), entity.getLastName(), entity.getSpecialization().getId(),
                entity.getIsActive(), responseDtos);
        log.info("Mapped UpdateTrainerProfileResponseDto: {}", responseDto);
        return responseDto;
    }

    /**
     * Maps a TrainerEntity to a RegistrationResponseDto.
     *
     * @param savedTrainer the TrainerEntity to map
     * @return the mapped RegistrationResponseDto
     */
    public RegistrationResponseDto trainerMapToResponse(TrainerEntity savedTrainer) {
        log.debug("Mapping TrainerEntity to RegistrationResponseDto for username: {}", savedTrainer.getUsername());
        RegistrationResponseDto responseDto = new RegistrationResponseDto(savedTrainer.getUsername(),
                savedTrainer.getPassword());
        log.info("Mapped RegistrationResponseDto: {}", responseDto);
        return responseDto;
    }
}
