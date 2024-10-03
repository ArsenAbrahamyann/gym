package org.example.gym.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.paylod.request.TraineeRegistrationRequestDto;
import org.example.gym.paylod.request.UpdateTraineeRequestDto;
import org.example.gym.paylod.request.UpdateTraineeTrainerListRequestDto;
import org.example.gym.paylod.response.GetTraineeProfileResponseDto;
import org.example.gym.paylod.response.RegistrationResponseDto;
import org.example.gym.paylod.response.TrainerListResponseDto;
import org.example.gym.paylod.response.TrainerResponseDto;
import org.example.gym.paylod.response.UpdateTraineeResponseDto;
import org.example.gym.service.TraineeService;
import org.example.gym.service.TrainerService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TraineeMapper {

    private final TrainerService trainerService;
    private final TraineeService traineeService;

    public TraineeEntity traineeRegistrationMapToEntity(TraineeRegistrationRequestDto requestDto) {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setAddress(requestDto.getAddress());
        trainee.setDateOfBirth(LocalDateTime.parse(requestDto.getDateOfBrith()));
        trainee.setLastName(requestDto.getLastName());
        trainee.setFirstName(requestDto.getFirsName());
        return trainee;
    }

    public RegistrationResponseDto traineeEntityMapToResponseDto(TraineeEntity traineeEntity) {
        RegistrationResponseDto responseDto = new RegistrationResponseDto(traineeEntity.getUsername(),
                traineeEntity.getPassword());
        return responseDto;
    }

    public GetTraineeProfileResponseDto traineeEntityMapToGetResponseTraineeDto(TraineeEntity trainee) {
        GetTraineeProfileResponseDto profileResponseDto = new GetTraineeProfileResponseDto();
        profileResponseDto.setAddress(trainee.getAddress());
        profileResponseDto.setActive(trainee.getIsActive());
        profileResponseDto.setDateOfBride(String.valueOf(trainee.getDateOfBirth()));
        profileResponseDto.setLastName(trainee.getLastName());
        profileResponseDto.setFirstName(trainee.getLastName());
        Set<TrainerEntity> trainers = trainee.getTrainers();
        Set<TrainerListResponseDto> trainerList = profileResponseDto.getTrainerList();
        for (TrainerEntity entity : trainers) {
            TrainerListResponseDto trainerListResponseDto = new TrainerListResponseDto(
                    entity.getUsername(), entity.getFirstName(),
                    entity.getLastName());
            trainerList.add(trainerListResponseDto);
        }
        profileResponseDto.setTrainerList(trainerList);

        return profileResponseDto;

    }

    public TraineeEntity updateDtoMapToTraineeEntity(UpdateTraineeRequestDto updateTraineeRequestDto) {
        TraineeEntity entity = traineeService.getTrainee(updateTraineeRequestDto.getUsername());
        entity.setDateOfBirth(LocalDateTime.parse(updateTraineeRequestDto.getDateOfBirth()));
        entity.setAddress(updateTraineeRequestDto.getAddress());
        entity.setUsername(updateTraineeRequestDto.getUsername());
        entity.setLastName(updateTraineeRequestDto.getLastName());
        entity.setFirstName(updateTraineeRequestDto.getFirstName());
        entity.setIsActive(updateTraineeRequestDto.isPublic());
        return entity;
    }

    public UpdateTraineeResponseDto traineeEntityMapToUpdateResponse(TraineeEntity trainee) {
        Set<TrainerEntity> trainers = trainee.getTrainers();
        Set<TrainerResponseDto> trainerList = new HashSet<>();
        for (TrainerEntity entity : trainers) {
            TrainerResponseDto trainerResponseDto = new TrainerResponseDto(entity.getUsername(), entity.getFirstName(),
                    entity.getLastName(), entity.getSpecialization().getId());
            trainerList.add(trainerResponseDto);
        }
        UpdateTraineeResponseDto responseDto = new UpdateTraineeResponseDto(trainee.getUsername(),
                trainee.getFirstName(), trainee.getLastName(), trainee.getDateOfBirth(), trainee.getAddress(),
                trainerList);
        return responseDto;

    }

    public List<TrainerResponseDto> mapToTrainerResponse(List<TrainerEntity> unassignedTrainers) {
        List<TrainerResponseDto> responseDtos = new ArrayList<>();
        for (TrainerEntity entity : unassignedTrainers) {
            TrainerResponseDto responseDto = new TrainerResponseDto(entity.getUsername(), entity.getFirstName(),
                    entity.getLastName(), entity.getSpecialization().getId());
            responseDtos.add(responseDto);
        }
        return responseDtos;
    }

    public TraineeEntity updateTraineeTrainerListMapToEntity(
            UpdateTraineeTrainerListRequestDto updateTraineeTrainerListRequestDto) {
        TraineeEntity trainee = traineeService.getTrainee(updateTraineeTrainerListRequestDto.getTraineeUsername());
        List<TrainerEntity> trainers = trainerService.findByUsernames(
                updateTraineeTrainerListRequestDto.getTrainerUsername());
        trainee.setTrainers(new HashSet<>(trainers));
        return trainee;
    }

    public List<TrainerResponseDto> updateTraineeTrainerListMapToTrainerResponse(TraineeEntity trainee) {
        List<TrainerResponseDto> responseDtos = new ArrayList<>();
        Set<TrainerEntity> trainers = trainee.getTrainers();
        for (TrainerEntity entity : trainers) {
            TrainerResponseDto responseDto = new TrainerResponseDto(entity.getUsername(), entity.getFirstName(),
                    entity.getLastName(), entity.getSpecialization().getId());
            responseDtos.add(responseDto);
        }
        return responseDtos;

    }
}
