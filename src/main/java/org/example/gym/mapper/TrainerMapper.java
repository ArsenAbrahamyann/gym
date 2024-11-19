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
import org.example.gym.entity.UserEntity;
import org.example.gym.utils.UserUtils;
import org.springframework.stereotype.Component;

/**
 * Mapper for Trainer-related DTOs and entities.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TrainerMapper {

    private final UserUtils userUtils;

    /**
     * Maps a TrainerRegistrationRequestDto to a TrainerEntity.
     *
     * @param registrationDto the registration request DTO
     * @return the mapped TrainerEntity
     */
    public TrainerEntity trainerRegistrationMapToEntity(TrainerRegistrationRequestDto registrationDto) {
        log.debug("Mapping TrainerRegistrationRequestDto to TrainerEntity: {}", registrationDto);
        TrainerEntity trainer = new TrainerEntity();
        UserEntity user = new UserEntity();
        TrainingTypeEntity trainingType = new TrainingTypeEntity();
        trainingType.setId(registrationDto.getTrainingTypeId());
        trainer.setSpecialization(trainingType);
        user.setIsActive(true);
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        String username = userUtils.generateUsername(registrationDto.getFirstName(),
                registrationDto.getLastName());
        String password = userUtils.generatePassword();
        user.setUsername(username);
        user.setPassword(password);
        trainer.setUser(user);
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
            UserEntity user = entity.getUser();
            TraineeListResponseDto responseDto = new TraineeListResponseDto(
                    user.getUsername(), user.getLastName(), user.getFirstName());
            traineeList.add(responseDto);
        }
        UserEntity user = trainer.getUser();
        GetTrainerProfileResponseDto responseDto = new GetTrainerProfileResponseDto(user.getFirstName(),
                user.getLastName(), trainer.getSpecialization().getId(), user.getIsActive(),
                traineeList);
        log.info("Mapped GetTrainerProfileResponseDto: {}", responseDto);
        return responseDto;
    }


    /**
     * Maps a TrainerEntity to an UpdateTrainerProfileResponseDto.
     *
     * @param trainer the TrainerEntity to map
     * @return the mapped UpdateTrainerProfileResponseDto
     */
    public UpdateTrainerProfileResponseDto updateTrainerProfileMapToResponseDto(TrainerEntity trainer) {
        log.debug("Mapping TrainerEntity to UpdateTrainerProfileResponseDto: {}", trainer);
        Set<TraineeEntity> trainees = trainer.getTrainees();
        List<TraineeListResponseDto> responseDtos = new ArrayList<>();
        for (TraineeEntity trainee : trainees) {
            UserEntity user = trainee.getUser();
            TraineeListResponseDto responseDto = new TraineeListResponseDto(user.getUsername(),
                    user.getLastName(), user.getFirstName());
            responseDtos.add(responseDto);
        }
        UserEntity user = trainer.getUser();
        UpdateTrainerProfileResponseDto responseDto = new UpdateTrainerProfileResponseDto(user.getUsername(),
                user.getFirstName(), user.getLastName(), trainer.getSpecialization().getId(),
                user.getIsActive(), responseDtos);
        log.info("Mapped UpdateTrainerProfileResponseDto: {}", responseDto);
        return responseDto;
    }

    /**
     * Maps a TrainerEntity to a RegistrationResponseDto.
     *
     * @param trainer the TrainerEntity to map
     * @return the mapped RegistrationResponseDto
     */
    public RegistrationResponseDto trainerMapToResponse(TrainerEntity trainer, String password) {
        log.debug("Mapping TrainerEntity to RegistrationResponseDto for username: {}",
                trainer.getUser().getUsername());
        UserEntity user = trainer.getUser();
        RegistrationResponseDto responseDto = new RegistrationResponseDto(user.getUsername(),
                password);
        log.info("Mapped RegistrationResponseDto: {}", responseDto);
        return responseDto;
    }
}
