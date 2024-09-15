package org.example.service;

import java.util.HashSet;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.TraineeRepository;
import org.example.repository.TrainerRepository;
import org.example.repository.UserRepository;
import org.example.utils.UserUtils;
import org.example.utils.ValidationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraineeService {
    private final UserUtils userUtils;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final ValidationUtils validationUtils;

    // 1. Create Trainee Profile
    @Transactional
    public TraineeDto createTraineeProfile(TraineeDto traineeDto) {
        log.info("Creating trainee profile ");

                List<String> allUsername = userRepository.findAllUsername()
                        .orElseThrow(() -> new ResourceNotFoundException("username not found"));
        String generatedUsername = userUtils.generateUsername(traineeDto.getUser().getFirstName(),
                traineeDto.getUser().getLastName(), allUsername);
        String generatedPassword = userUtils.generatePassword();

        // Create User entity
        UserEntity user = new UserEntity();
        user.setUsername(generatedUsername);
        user.setPassword(generatedPassword);
        user.setFirstName(traineeDto.getUser().getFirstName());
        user.setLastName(traineeDto.getUser().getLastName());
        user.setIsActive(traineeDto.getUser().getIsActive());
        userRepository.save(user);

        // Create Trainee entity and associate with User
        TraineeEntity trainee = new TraineeEntity();
        trainee.setDateOfBirth(traineeDto.getDateOfBirth());
        trainee.setAddress(traineeDto.getAddress());
        trainee.setUser(user);
        traineeRepository.save(trainee);

        validationUtils.validateTrainee(trainee);

        log.info("Trainee profile created successfully for {}", generatedUsername);

        return traineeDto;
    }

    // 7. Change Trainee Password
    public void changeTraineePassword(String username, String newPassword) {
        log.info("Changing password for trainee {}", username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found with username: "
                        + username));

        validationUtils.validatePasswordMatch(user, newPassword);

        user.setPassword(newPassword);
        userRepository.update(user);
        log.info("Password updated successfully for trainee {}", username);
    }

    // 11. Activate/Deactivate Trainee
    @Transactional
    public void toggleTraineeStatus(String username) {
        log.info("Toggling trainee status for {}", username);
        TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found with username: "
                        + username));

        validationUtils.validateActivateDeactivateTrainee(trainee);

        UserEntity user = trainee.getUser();
        user.setIsActive(! user.getIsActive());
        userRepository.update(user);
        log.info("Trainee status toggled successfully for {}", username);
    }

    public List<TrainerEntity> getUnassignedTrainers(String traineeUsername) {
        log.info("Fetching unassigned trainers for trainee: {}", traineeUsername);
        TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(traineeUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found"));

        List<TrainerEntity> allTrainers = trainerRepository.findAll()
                .orElseThrow(() -> new EntityNotFoundException("Trainers not found"));
        List<TrainerEntity> assignedTrainers = trainerRepository.findAssignedTrainers(trainee.getId())
                .orElseThrow(() -> new EntityNotFoundException("Trainees not found"));

        allTrainers.removeAll(assignedTrainers);


        log.info("Found {} unassigned trainers for trainee: {}", allTrainers.size(), traineeUsername);
        return allTrainers;
    }

    @Transactional
    public void updateTraineeTrainers(String traineeUsername, List<Long> trainerIds) {
        log.info("Updating trainers for trainee: {}", traineeUsername);
        TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(traineeUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found"));

        List<TrainerEntity> newTrainers = trainerRepository.findAllById(trainerIds)
                .orElseThrow(() -> new EntityNotFoundException("Trainers not found"));

        validationUtils.validateUpdateTraineeTrainerList(trainee,newTrainers);

        trainee.setTrainers(new HashSet<>(newTrainers));

        newTrainers.forEach(validationUtils::validateTrainer);

        traineeRepository.update(trainee);
        log.info("Updated trainer list for trainee: {}", traineeUsername);
    }

    // 10. Update Trainee Profile
    @Transactional
    public void updateTraineeProfile(String username, TraineeDto traineeDto) {
        log.info("Updating trainee profile for {}", username);
        TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found with username: "
                        + username));

        trainee.setDateOfBirth(traineeDto.getDateOfBirth());
        trainee.setAddress(traineeDto.getAddress());

        validationUtils.validateUpdateTrainee(trainee);

        traineeRepository.update(trainee);

        log.info("Trainee profile updated successfully for {}", username);
    }

}
