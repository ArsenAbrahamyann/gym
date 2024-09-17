package org.example.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.entity.UserEntity;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.TrainerRepository;
import org.example.repository.TrainingRepository;
import org.example.repository.TrainingTypeRepository;
import org.example.repository.UserRepository;
import org.example.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing Trainer profiles and related operations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final ValidationUtils validationUtils;
    private final ModelMapper modelMapper;

    /**
     * Creates a new trainer profile.
     *
     * @param trainerDto The data transfer object containing trainer information.
     * @return The created TrainerDto object.
     */
    @Transactional
    public TrainerDto createTrainerProfile(TrainerDto trainerDto) {
        log.info("Creating trainer profile ");
        TrainerEntity trainer = modelMapper.map(trainerDto, TrainerEntity.class);
        validationUtils.validateTrainer(trainer);
        TrainingTypeEntity trainingType = trainer.getSpecialization();

        if (trainingType.getId() == null) {
            trainingTypeRepository.save(trainingType);
        }

        Optional<UserEntity> existingUser = userRepository.findByUsername(trainerDto.getUser().getUsername());
        if (existingUser.isPresent()) {
            trainer.getUser().setId(existingUser.get().getId());
        }else {
            userRepository.save(trainer.getUser());
        }

        trainerRepository.save(trainer);
        log.debug("Trainer profile created: {}", trainer);
        return trainerDto;
    }

    /**
     * Changes the password of the trainer.
     *
     * @param username    The username of the trainer.
     * @param newPassword The new password to set.
     */
    @Transactional
    public void changeTrainerPassword(String username, String newPassword) {
        log.info("Changing password for trainer {}", username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found with username: "
                        + username));

        validationUtils.validatePasswordMatch(user, newPassword);
        user.setPassword(newPassword);
        userRepository.update(user);
        log.info("Password updated successfully for trainer {}", username);
    }

    /**
     * Toggles the active status of a trainer.
     *
     * @param username The username of the trainer.
     */
    @Transactional
    public void toggleTrainerStatus(String username) {
        log.info("Toggling trainer status for {}", username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found with username: "
                        + username));

//        validationUtils.validateActivateDeactivateTrainer(trainer);
        user.setIsActive(! user.getIsActive());
        userRepository.update(user);
        log.info("Trainer status toggled successfully for {}", username);
    }

    /**
     * Updates the profile of the trainer.
     *
     * @param username   The username of the trainer.
     * @param trainerDto The updated trainer information.
     */
    @Transactional
    public void updateTrainerProfile(String username, TrainerDto trainerDto) {
        log.info("Updating trainer profile for {}", username);
        TrainerEntity trainer = trainerRepository.findByTrainerFromUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found with username: "
                        + username));
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        validationUtils.validateUpdateTrainer(trainer);
        TrainingTypeEntity trainingType = modelMapper.map(trainerDto.getSpecialization().getTrainingTypeName(),
                TrainingTypeEntity.class);
        trainingType.setTrainingTypeName(trainerDto.getSpecialization().getTrainingTypeName());
        trainingTypeRepository.save(trainingType);
        trainer.setSpecialization(trainingType);
        trainer.setUser(user);
        trainerRepository.update(trainer);

        log.info("Trainer profile updated successfully for {}", username);
    }

    /**
     * Retrieves the list of trainings for a trainer within a given date range.
     *
     * @param trainerUsername The username of the trainer.
     * @param fromDate        The start date of the date range.
     * @param toDate          The end date of the date range.
     * @param traineeName     The name of the trainee (optional).
     * @return The list of trainings for the trainer.
     */
    public List<TrainingEntity> getTrainerTrainings(String trainerUsername, Date fromDate,
                                                    Date toDate, String traineeName) {
        log.info("Fetching trainings for trainer: {}", trainerUsername);
        TrainerEntity trainer = trainerRepository.findByTrainerFromUsername(trainerUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found with username: "
                        + trainerUsername));
        validationUtils.validateTrainerTrainingsCriteria(trainerUsername, fromDate, toDate, traineeName);

        List<TrainingEntity> trainings = trainingRepository.findTrainingsForTrainer(
                        trainer.getId(), fromDate, toDate, traineeName)
                .orElseThrow(() -> new ResourceNotFoundException("Trainings not found"));

        log.info("Found {} trainings for trainer: {}", trainings.size(), trainerUsername);
        return trainings;
    }
}
