package org.example.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.exeption.ResourceNotFoundException;
import org.example.paylod.request.TraineeRegistrationRequestDto;
import org.example.paylod.request.UserLoginRequestDto;
import org.example.paylod.response.GetTraineeProfileResponseDto;
import org.example.paylod.response.RegistrationResponseDto;
import org.example.paylod.response.TrainerListResponseDto;
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

    /**
     * Constructs a new {@code TraineeService} instance with dependencies injected for handling trainee-related operations.
     * The {@link TrainerService} is injected lazily to avoid circular dependency issues that could arise from direct
     * initialization. This constructor also accepts repositories and utilities needed for performing trainee-related tasks.
     *
     * @param trainerService     the {@link TrainerService} used for managing trainer-related functionalities.
     *                           This service is injected lazily to prevent circular dependency.
     * @param traineeRepository  the {@link TraineeRepository} used for performing CRUD operations on trainee entities.
     * @param userService        the {@link UserService} responsible for handling user-related operations.
     * @param validationUtils    the {@link ValidationUtils} used for validating trainee data during service operations.
     * @param modelMapper        the {@link ModelMapper} used for mapping between DTOs and entities.
     */
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
     * @param registrationRequestDto The data transfer object containing trainee information.
     * @return The created TraineeDto object.
     */
    @Transactional
    public RegistrationResponseDto createTraineeProfile(TraineeRegistrationRequestDto registrationRequestDto) {
        log.info("Creating trainee profile");
        try {
            TraineeEntity trainee = modelMapper.map(registrationRequestDto, TraineeEntity.class);
            UserEntity user = trainee.getUser();
            Optional<UserEntity> existingUserOpt = userService.findByUsername(user.getUsername());
            if (existingUserOpt.isPresent()) {
                user = existingUserOpt.get();
            } else {
                userService.save(user);
            }
            trainee.setUser(user);
            UserLoginRequestDto loginRequestDto = new UserLoginRequestDto(trainee.getUser().getUsername(),
                    trainee.getUser().getPassword());
            userService.authenticateUser(loginRequestDto);
            traineeRepository.save(trainee);
            log.info("Trainee profile created successfully for {}", user.getUsername());
            RegistrationResponseDto responseDto = new RegistrationResponseDto(loginRequestDto.getUsername(),
                    loginRequestDto.getPassword());
            return responseDto;
        } catch (RuntimeException e) {
            log.error("Failed to create trainee profile: {}", e.getMessage());
            return null;
        }
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
        try {
            UserEntity user = userService.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Trainee not found with username: " + username));

            validationUtils.validatePasswordMatch(user, newPassword);
            user.setPassword(newPassword);
            userService.update(user);
            log.info("Password updated successfully for trainee {}", username);
        } catch (Exception e) {
            log.error("Failed to change password for trainee {}: {}", username, e.getMessage());
        }
    }

    /**
     * Toggles the active status of a trainee.
     *
     * @param username The username of the trainee.
     */
    @Transactional
    public void toggleTraineeStatus(String username) {
        log.info("Toggling trainee status for {}", username);
        try {
            UserEntity user = userService.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Trainee not found with username: " + username));

            user.setIsActive(!user.getIsActive());
            userService.update(user);
            log.info("Trainee status toggled successfully for {}", username);
        } catch (Exception e) {
            log.error("Failed to toggle trainee status for {}: {}", username, e.getMessage());
        }
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

        try {
            TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(traineeUsername)
                    .orElseThrow(() -> new ResourceNotFoundException("Trainee not found for username: "
                            + traineeUsername));

            List<TrainerEntity> allTrainers = trainerService.findAll();
            if (allTrainers == null) {
                log.info("Trainers not found.");
            }

            List<TrainerEntity> assignedTrainers = trainerService.findAssignedTrainers(trainee.getId());
            if (assignedTrainers.isEmpty()) {
                log.info("Assigned trainers not found for trainee ID: "
                        + trainee.getId());
            }

            allTrainers.removeAll(assignedTrainers);
            log.info("Found {} unassigned trainers for trainee: {}", allTrainers.size(), traineeUsername);
            return allTrainers;
        } catch (Exception e) {
            log.error("Failed to fetch unassigned trainers for {}: {}", traineeUsername, e.getMessage());
            return List.of();
        }

    }

    /**
     * Updates the list of trainers assigned to a specific trainee.
     *
     * @param traineeUsername The username of the trainee.
     * @param trainerIds      The list of trainer IDs to be assigned to the trainee.
     */
    @Transactional
    public void updateTraineeTrainers(String traineeUsername, List<Long> trainerIds) {
        log.info("Updating trainers for trainee: {}", traineeUsername);
        try {
            TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(traineeUsername)
                    .orElseThrow(() -> new ResourceNotFoundException("Trainee not found"));

            List<TrainerEntity> newTrainers = trainerService.findAllById(trainerIds);
            if (newTrainers.isEmpty()) {
                log.info("Trainers not found");
            }

            validationUtils.validateUpdateTraineeTrainerList(trainee, newTrainers);
            trainee.setTrainers(new HashSet<>(newTrainers));

            newTrainers.forEach(validationUtils::validateTrainer);
            traineeRepository.update(trainee);

            log.info("Successfully updated trainer list for trainee: {}", traineeUsername);
        } catch (Exception e) {
            log.error("Failed to update trainers for {}: {}", traineeUsername, e.getMessage());
        }
    }

    /**
     * Updates the profile information of a trainee.
     *
     * @param username   The username of the trainee.
     * @param traineeDto The updated trainee information.
     */
    @Transactional
    public void updateTraineeProfile(String username, TraineeDto traineeDto) {
        log.info("Updating trainee profile for {}", username);

        try {
            TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Trainee not found with username: " + username));
            UserEntity existingUser = userService.findByUsername(trainee.getUser().getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            trainee.setUser(existingUser);
            trainee.setDateOfBirth(traineeDto.getDateOfBirth());
            trainee.setAddress(traineeDto.getAddress());

            validationUtils.validateUpdateTrainee(trainee);
            traineeRepository.update(trainee);
            log.info("Trainee profile updated successfully for {}", username);
        } catch (Exception e) {
            log.error("Failed to update trainee profile for {}: {}", username, e.getMessage());
        }
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
        try {
            UserEntity user = userService.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(user.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("TraineeEntity not found"));

            traineeRepository.delete(trainee);
            log.info("Trainee deleted successfully with username: {}", username);
        } catch (Exception e) {
            log.error("Failed to delete trainee with username {}: {}", username, e.getMessage());
        }
    }

    /**
     * Finds a {@link TraineeEntity} by its unique ID.
     * This method is transactional, ensuring that the database operations are
     * executed in a single transaction, and also uses {@code @SneakyThrows} to
     * automatically handle checked exceptions that may be thrown during the
     * execution.
     *
     * @param traineeId the unique ID of the trainee to find.
     * @return an {@link Optional} containing the {@link TraineeEntity} if found,
     *         or an empty {@link Optional} if no trainee is found with the given ID.
     */
    @Transactional
    public Optional<TraineeEntity> findById(Long traineeId) {
        try {
            return traineeRepository.findById(traineeId);
        } catch (Exception e) {
            log.error("Error retrieving trainee by ID {}: {}", traineeId, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Finds a {@link TraineeEntity} by its username.
     * This method is transactional, ensuring that the database operations are
     * executed in a single transaction, and also uses {@code @SneakyThrows} to
     * automatically handle checked exceptions that may be thrown during the
     * execution.
     *
     * @param traineeName the username of the trainee to find.
     * @return an {@link Optional} containing the {@link TraineeEntity} if found,
     *         or an empty {@link Optional} if no trainee is found with the given username.
     */
    @Transactional
    public GetTraineeProfileResponseDto getTrainee(String traineeName) {
        try {
            Optional<TraineeEntity> byTraineeFromUsername = traineeRepository.findByTraineeFromUsername(traineeName);
            GetTraineeProfileResponseDto profileResponseDto = new GetTraineeProfileResponseDto();
            if (byTraineeFromUsername.isPresent()) {
                TraineeEntity trainee = byTraineeFromUsername.get();
                profileResponseDto.setAddress(trainee.getAddress());
                profileResponseDto.setActive(trainee.getUser().getIsActive());
                profileResponseDto.setDateOfBride(String.valueOf(trainee.getDateOfBirth()));
                profileResponseDto.setLastName(trainee.getUser().getLastName());
                profileResponseDto.setFirstName(trainee.getUser().getLastName());
                Set<TrainerEntity> trainers = trainee.getTrainers();
                Set<TrainerListResponseDto> trainerList = profileResponseDto.getTrainerList();
                for (TrainerEntity entity : trainers) {
                    TrainerListResponseDto trainerListResponseDto = new TrainerListResponseDto(
                            entity.getUser().getUsername(), entity.getUser().getFirstName(),
                            entity.getUser().getLastName());
                    trainerList.add(trainerListResponseDto);
                }
                profileResponseDto.setTrainerList(trainerList);
            }
            return profileResponseDto;
        } catch (Exception e) {
            log.error("Error retrieving trainee by username {}: {}", traineeName, e.getMessage());
            return null;
        }
    }

}
