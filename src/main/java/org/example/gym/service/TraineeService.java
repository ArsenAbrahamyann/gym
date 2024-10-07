package org.example.gym.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.ResourceNotFoundException;
import org.example.gym.exeption.TraineeNotFoundException;
import org.example.gym.paylod.request.ActivateRequestDto;
import org.example.gym.repository.TraineeRepository;
import org.example.gym.utils.UserUtils;
import org.example.gym.utils.ValidationUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing Trainee profiles and related operations.
 */
@Service
@Slf4j
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerService trainerService;
    private final UserService userService;
    private final ValidationUtils validationUtils;
    private final UserUtils userUtils;

    public TraineeService(@Lazy TrainerService trainerService, TraineeRepository traineeRepository,
                          UserService userService, ValidationUtils validationUtils, UserUtils userUtils) {
        this.trainerService = trainerService;
        this.traineeRepository = traineeRepository;
        this.userService = userService;
        this.validationUtils = validationUtils;
        this.userUtils = userUtils;
    }

    @Transactional
    public TraineeEntity createTraineeProfile(TraineeEntity trainee) {
        log.info("Creating trainee profile");
        List<String> allUsernames = userService.findAllUsernames();
        String generateUsername = userUtils.generateUsername(trainee.getFirstName(), trainee.getLastName(), allUsernames);
        trainee.setUsername(generateUsername);
        String generatePassword = userUtils.generatePassword();
        trainee.setPassword(generatePassword);
        validationUtils.validateTrainee(trainee);
        traineeRepository.save(trainee);
        userService.authenticateUser(trainee.getUsername(), trainee.getPassword());
        log.info("Trainee profile created successfully for {}", trainee.getUsername());
        return trainee;
    }

    @Transactional
    public void changeTraineePassword(String username, String newPassword) {
        log.info("Changing password for trainee {}", username);
        UserEntity user = userService.findByUsername(username);

        validationUtils.validatePasswordMatch(user, newPassword);
        user.setPassword(newPassword);
        userService.update(user);
        log.info("Password updated successfully for trainee {}", username);
    }

    @Transactional
    public void toggleTraineeStatus(ActivateRequestDto requestDto) {
        log.info("Toggling trainee status for {}", requestDto.getUsername());
        TraineeEntity trainee = getTrainee(requestDto.getUsername());
        trainee.setIsActive(requestDto.isPublic());
        traineeRepository.save(trainee);
        log.info("Trainee status toggled successfully for {}", requestDto.getUsername());
    }

    @Transactional
    public List<TrainerEntity> getUnassignedTrainers(String traineeUsername) {
        log.info("Fetching unassigned trainers for trainee: {}", traineeUsername);

        TraineeEntity trainee = traineeRepository.findByUsername(traineeUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found for username: " + traineeUsername));

        List<TrainerEntity> allTrainers = trainerService.findAll();
        List<TrainerEntity> assignedTrainers = trainerService.findAssignedTrainers(trainee.getId());

        allTrainers.removeAll(assignedTrainers);
        log.info("Found {} unassigned trainers for trainee: {}", allTrainers.size(), traineeUsername);
        return allTrainers;
    }

    @Transactional
    public TraineeEntity updateTraineeTrainers(TraineeEntity trainee) {
        log.info("Updating trainers for trainee: {}", trainee.getUsername());
        Set<TrainerEntity> trainers = trainee.getTrainers();
        validationUtils.validateUpdateTraineeTrainerList(trainee, trainers);
        trainers.forEach(validationUtils::validateTrainer);
        TraineeEntity traineeEntity = traineeRepository.save(trainee);
        log.info("Successfully updated trainer list for trainee: {}", trainee.getUsername());
        return traineeEntity;
    }

    @Transactional
    public TraineeEntity updateTraineeProfile(TraineeEntity traineeEntity) {
        log.info("Updating trainee profile for {}", traineeEntity.getUsername());
        validationUtils.validateUpdateTrainee(traineeEntity);
        TraineeEntity trainee = traineeRepository.save(traineeEntity);

        log.info("Trainee profile updated successfully for {}", traineeEntity.getUsername());
        return trainee;
    }

    @Transactional
    public void deleteTraineeByUsername(String username) {
        log.info("Deleting trainee with username: {}", username);
        UserEntity user = userService.findByUsername(username);

        TraineeEntity trainee = traineeRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("TraineeEntity not found"));

        traineeRepository.delete(trainee);
        log.info("Trainee deleted successfully with username: {}", username);
    }

    @Transactional
    public TraineeEntity findById(Long traineeId) {
        return traineeRepository.findById(traineeId)
                .orElseThrow(() -> new TraineeNotFoundException("Trainee not found for traineeId: " + traineeId));
    }

    @Transactional
    public TraineeEntity getTrainee(String username) {
        return traineeRepository.findByUsername(username)
                .orElseThrow(() -> new TraineeNotFoundException("Trainee not found for username: " + username));
    }

}
