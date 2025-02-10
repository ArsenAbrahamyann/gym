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
import org.example.gym.entity.UserEntity;
import org.example.gym.utils.UserUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TraineeMapper {

    private final UserUtils userUtils;

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
        UserEntity user = new UserEntity();
        user.setIsActive(true);
        user.setLastName(requestDto.getLastName());
        user.setFirstName(requestDto.getFirsName());
        String username = userUtils.generateUsername(requestDto.getFirsName(),
                requestDto.getLastName());
        String password = userUtils.generatePassword();
        user.setUsername(username);
        user.setPassword(password);
        trainee.setUser(user);
        log.info("Mapped TraineeRegistrationRequestDto to TraineeEntity: {}", trainee);
        return trainee;
    }

    /**
     * Maps a TraineeEntity to a RegistrationResponseDto.
     *
     * @param traineeEntity the TraineeEntity to map
     * @return the mapped RegistrationResponseDto
     */
    public RegistrationResponseDto traineeEntityMapToResponseDto(TraineeEntity traineeEntity, String password) {
        UserEntity user = traineeEntity.getUser();
        RegistrationResponseDto responseDto = new RegistrationResponseDto(user.getUsername(), password);
        log.info("Mapped TraineeEntity to RegistrationResponseDto!");
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
        UserEntity traineeUser = trainee.getUser();
        profileResponseDto.setAddress(trainee.getAddress());
        profileResponseDto.setActive(traineeUser.getIsActive());
        profileResponseDto.setDateOfBride(trainee.getDateOfBirth());
        profileResponseDto.setLastName(traineeUser.getLastName());
        profileResponseDto.setFirstName(traineeUser.getFirstName());
        Set<TrainerEntity> trainers = trainee.getTrainers();
        Set<TrainerListResponseDto> trainerList = new HashSet<>();

        for (TrainerEntity entity : trainers) {
            UserEntity user = entity.getUser();
            TrainerListResponseDto trainerListResponseDto = new TrainerListResponseDto(
                    user.getUsername(), user.getFirstName(),
                    user.getLastName());
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
            UserEntity user = entity.getUser();
            TrainerResponseDto trainerResponseDto = new TrainerResponseDto(user.getUsername(), user.getFirstName(),
                    user.getLastName(), entity.getSpecialization().getId());
            trainerList.add(trainerResponseDto);
        }
        UserEntity user = trainee.getUser();
        UpdateTraineeResponseDto responseDto = new UpdateTraineeResponseDto(user.getUsername(),
               user.getFirstName(), user.getLastName(), trainee.getDateOfBirth(), trainee.getAddress(),
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
                .map(trainer -> new TrainerResponseDto(trainer.getUser().getUsername(),
                        trainer.getUser().getFirstName(),
                        trainer.getUser().getLastName(),
                        trainer.getSpecialization().getId()))
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
            UserEntity user = entity.getUser();
            TrainerResponseDto responseDto = new TrainerResponseDto(user.getUsername(), user.getFirstName(),
                    user.getLastName(), entity.getSpecialization().getId());
            responseDtos.add(responseDto);
        }

        log.info("Mapped TrainerResponseDtos for Trainee: {}", trainee.getUser().getUsername());
        return responseDtos;
    }
}
