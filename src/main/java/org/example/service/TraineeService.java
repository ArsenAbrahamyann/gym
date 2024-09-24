package org.example.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.TraineeRepository;
import org.example.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public TraineeService(@Lazy TrainerService trainerService, TraineeRepository traineeRepository,
                          UserService userService, ValidationUtils validationUtils, ModelMapper modelMapper) {
        this.trainerService = trainerService;
        this.traineeRepository = traineeRepository;
        this.userService = userService;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;
    }


    /**
     * Creates a new trainee profile.
     *
     * @param traineeDto The data transfer object containing trainee information.
     * @return The created TraineeDto object.
     */
    @Transactional
    public TraineeDto createTraineeProfile(TraineeDto traineeDto) {
        log.info("Creating trainee profile");

        TraineeEntity trainee = modelMapper.map(traineeDto, TraineeEntity.class);

        UserEntity user = trainee.getUser();
        Optional<UserEntity> existingUserOpt = userService.findByUsername(user.getUsername());
        if (existingUserOpt.isPresent()) {
            user = existingUserOpt.get();
        } else {
            userService.save(user);
        }

        trainee.setUser(user);

        userService.authenticateUser(user.getUsername(), user.getPassword());
        traineeRepository.save(trainee);

        log.info("Trainee profile created successfully for {}", user.getUsername());

        return traineeDto;
    }

    /**
     * Changes the password of a trainee.
     *
     * @param username    The username of the trainee.
     * @param newPassword The new password to set.
     */
    @Transactional
    public void changeTraineePassword(String username, String newPassword) {
        log.info("Changing password for trainee {}", username);
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found with username: "
                        + username));

        validationUtils.validatePasswordMatch(user, newPassword);

        user.setPassword(newPassword);
        userService.update(user);
        log.info("Password updated successfully for trainee {}", username);
    }

    /**
     * Toggles the active status of a trainee.
     *
     * @param username The username of the trainee.
     */
    @Transactional
    public void toggleTraineeStatus(String username) {
        log.info("Toggling trainee status for {}", username);
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found with username: "
                        + username));

        user.setIsActive(!user.getIsActive());
        userService.update(user);
        log.info("Trainee status toggled successfully for {}", username);
    }

    /**
     * Retrieves unassigned trainers for a trainee.
     *
     * @param traineeUsername The username of the trainee.
     * @return The list of unassigned trainers.
     */
    @Transactional
    public List<TrainerEntity> getUnassignedTrainers(String traineeUsername) {
        log.info("Fetching unassigned trainers for trainee: {}", traineeUsername);
        TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(traineeUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found"));

        List<TrainerEntity> allTrainers = trainerService.findAll()
                .orElseThrow(() -> new EntityNotFoundException("Trainers not found"));
        List<TrainerEntity> assignedTrainers = trainerService.findAssignedTrainers(trainee.getId())
                .orElseThrow(() -> new EntityNotFoundException("Trainees not found"));

        allTrainers.removeAll(assignedTrainers);

        log.info("Found {} unassigned trainers for trainee: {}", allTrainers.size(), traineeUsername);
        return allTrainers;
    }

    /**
     * Updates the list of trainers assigned to a specific trainee.
     *
     * @param traineeUsername The username of the trainee.
     * @param trainerIds      The list of trainer IDs to be assigned to the trainee.
     * @throws EntityNotFoundException If the trainee or any trainers are not found.
     */
    @Transactional
    public void updateTraineeTrainers(String traineeUsername, List<Long> trainerIds) {
        log.info("Updating trainers for trainee: {}", traineeUsername);
        TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(traineeUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found"));

        List<TrainerEntity> newTrainers = trainerService.findAllById(trainerIds)
                .orElseThrow(() -> new EntityNotFoundException("Trainers not found"));

        validationUtils.validateUpdateTraineeTrainerList(trainee, newTrainers);

        trainee.setTrainers(new HashSet<>(newTrainers));

        newTrainers.forEach(validationUtils::validateTrainer);

        traineeRepository.update(trainee);
        log.info("Updated trainer list for trainee: {}", traineeUsername);
    }

    /**
     * Updates the profile information of a trainee.
     *
     * @param username   The username of the trainee.
     * @param traineeDto The updated trainee information.
     * @throws EntityNotFoundException If the trainee is not found.
     */
    @Transactional
    public void updateTraineeProfile(String username, TraineeDto traineeDto) {
        log.info("Updating trainee profile for {}", username);

        TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found with username: "
                        + username));
        UserEntity existingUser = userService.findByUsername(trainee.getUser().getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        trainee.setUser(existingUser);
        trainee.setDateOfBirth(traineeDto.getDateOfBirth());
        trainee.setAddress(traineeDto.getAddress());

        validationUtils.validateUpdateTrainee(trainee);

        traineeRepository.update(trainee);

        log.info("Trainee profile updated successfully for {}", username);
    }

    /**
     * Deletes a trainee by their username.
     *
     * @param username The username of the trainee to delete.
     * @throws ResourceNotFoundException If the user or trainee is not found.
     */
    @Transactional
    public void deleteTraineeByUsername(String username) {
        log.info("Deleting trainee with username: {}", username);
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user != null) {
            TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(user.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("TraineeEntity not found"));

            if (trainee != null) {
                traineeRepository.delete(trainee);
            } else {
                throw new IllegalArgumentException("Trainee not found for username: " + username);
            }
        } else {
            throw new IllegalArgumentException("User not found for username: " + username);
        }
        log.info("Trainee deleted successfully with username: {}", username);
    }

    @Transactional
    public Optional<TraineeEntity> findById(Long traineeId) {
        return traineeRepository.findById(traineeId);
    }

    @Transactional
    public Optional<TraineeEntity> findByTraineeFromUsername(String traineeName) {
        return traineeRepository.findByTraineeFromUsername(traineeName);
    }
}
