package org.example.gym.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.TraineeRegistrationRequestDto;
import org.example.gym.dto.response.GetTraineeProfileResponseDto;
import org.example.gym.dto.response.RegistrationResponseDto;
import org.example.gym.dto.response.TrainerListResponseDto;
import org.example.gym.dto.response.TrainerResponseDto;
import org.example.gym.dto.response.UpdateTraineeResponseDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TraineeMapper {

    /**
     * Maps a TraineeRegistrationRequestDto to a TraineeEntity.
     *
     * @param requestDto the request DTO to map
     * @return the mapped TraineeEntity
     */
    public TraineeEntity traineeRegistrationMapToEntity(TraineeRegistrationRequestDto requestDto) {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setAddress(requestDto.getAddress());
        trainee.setDateOfBirth(requestDto.getDateOfBrith());
        trainee.setIsActive(true);
        trainee.setLastName(requestDto.getLastName());
        trainee.setFirstName(requestDto.getFirsName());
        log.info("Mapped TraineeRegistrationRequestDto to TraineeEntity: {}", trainee);
        return trainee;
    }

    /**
     * Maps a TraineeEntity to a RegistrationResponseDto.
     *
     * @param traineeEntity the TraineeEntity to map
     * @return the mapped RegistrationResponseDto
     */
    public RegistrationResponseDto traineeEntityMapToResponseDto(TraineeEntity traineeEntity) {
        RegistrationResponseDto responseDto = new RegistrationResponseDto(traineeEntity.getUsername(),
                traineeEntity.getPassword());
        log.info("Mapped TraineeEntity to RegistrationResponseDto: {}", responseDto);
        return responseDto;
    }

    /**
     * Maps a TraineeEntity to a GetTraineeProfileResponseDto.
     *
     * @param trainee the TraineeEntity to map
     * @return the mapped GetTraineeProfileResponseDto
     */
    public GetTraineeProfileResponseDto traineeEntityMapToGetResponseTraineeDto(TraineeEntity trainee) {
        GetTraineeProfileResponseDto profileResponseDto = new GetTraineeProfileResponseDto();
        profileResponseDto.setAddress(trainee.getAddress());
        profileResponseDto.setActive(trainee.getIsActive());
        profileResponseDto.setDateOfBride(String.valueOf(trainee.getDateOfBirth()));
        profileResponseDto.setLastName(trainee.getLastName());
        profileResponseDto.setFirstName(trainee.getFirstName());
        Set<TrainerEntity> trainers = trainee.getTrainers();
        Set<TrainerListResponseDto> trainerList = new HashSet<>();

        for (TrainerEntity entity : trainers) {
            TrainerListResponseDto trainerListResponseDto = new TrainerListResponseDto(
                    entity.getUsername(), entity.getFirstName(),
                    entity.getLastName());
            trainerList.add(trainerListResponseDto);
        }
        profileResponseDto.setTrainerList(trainerList);

        log.info("Mapped TraineeEntity to GetTraineeProfileResponseDto: {}", profileResponseDto);
        return profileResponseDto;

    }


    /**
     * Maps a TraineeEntity to an UpdateTraineeResponseDto.
     *
     * @param trainee the TraineeEntity to map
     * @return the mapped UpdateTraineeResponseDto
     */
    public UpdateTraineeResponseDto traineeEntityMapToUpdateResponse(TraineeEntity trainee) {
        Set<TrainerEntity> trainers = trainee.getTrainers();
        Set<TrainerResponseDto> trainerList = new HashSet<>();
        for (TrainerEntity entity : trainers) {
            TrainerResponseDto trainerResponseDto = new TrainerResponseDto(entity.getUsername(), entity.getFirstName(),
                    entity.getLastName(), entity.getSpecialization().getId());
            trainerList.add(trainerResponseDto);
        }
        UpdateTraineeResponseDto responseDto = new UpdateTraineeResponseDto(trainee.getUsername(),
               trainee.getFirstName(), trainee.getLastName(), trainee.getDateOfBirth().toString(), trainee.getAddress(),
               trainerList);
        log.info("Mapped TraineeEntity to UpdateTraineeResponseDto: {}", responseDto);
        return responseDto;

    }

    /**
     * Maps a list of TrainerEntity to a list of TrainerResponseDto.
     *
     * @param trainers the list of TrainerEntity to map
     * @return the list of mapped TrainerResponseDto
     */
    public List<TrainerResponseDto> mapToTrainerResponse(List<TrainerEntity> trainers) {
        return trainers.stream()
                .map(trainer -> new TrainerResponseDto(trainer.getUsername(), trainer.getFirstName(),
                        trainer.getLastName(), trainer.getSpecialization().getId()))
                .collect(Collectors.toList());


    }

    /**
     * Maps a TraineeEntity's trainers to a list of TrainerResponseDto.
     *
     * @param trainee the TraineeEntity to map
     * @return the list of mapped TrainerResponseDto
     */
    public List<TrainerResponseDto> updateTraineeTrainerListMapToTrainerResponse(TraineeEntity trainee) {
        List<TrainerResponseDto> responseDtos = new ArrayList<>();
        Set<TrainerEntity> trainers = trainee.getTrainers();
        for (TrainerEntity entity : trainers) {
            TrainerResponseDto responseDto = new TrainerResponseDto(entity.getUsername(), entity.getFirstName(),
                    entity.getLastName(), entity.getSpecialization().getId());
            responseDtos.add(responseDto);
        }

        log.info("Mapped TrainerResponseDtos for Trainee: {}", trainee.getUsername());
        return responseDtos;
    }
}
