package org.example.service;

import java.util.Date;
import java.util.List;
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
import org.example.repository.UserRepository;
import org.example.utils.UserUtils;
import org.example.utils.ValidationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final UserUtils userUtils;
    private final TrainingRepository trainingRepository;
    private final ValidationUtils validationUtils;

    public TrainerDto createTrainerProfile(TrainerDto trainerDto) {
        log.info("Creating trainer profile ");


        List<String> allUsername = userRepository.findAllUsername()
                .orElseThrow(() -> new ResourceNotFoundException("username not found"));
        String generatedUsername = userUtils.generateUsername(trainerDto.getUser().getFirstName(),
                trainerDto.getUser().getLastName(), allUsername);
        String generatedPassword = userUtils.generatePassword();

        // Create User entity
        UserEntity user = new UserEntity();
        user.setUsername(generatedUsername);
        user.setPassword(generatedPassword);
        user.setFirstName(trainerDto.getUser().getFirstName());
        user.setLastName(trainerDto.getUser().getLastName());
        user.setIsActive(trainerDto.getUser().getIsActive());
        userRepository.save(user);

        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setTrainingTypeName(trainerDto.getSpecialization().getTrainingTypeName());

        // Create Trainee entity and associate with User
        TrainerEntity trainer = new TrainerEntity();
        trainer.setSpecialization(trainingTypeEntity);
        trainer.setTrainees(trainerDto.getTrainees());
        trainer.setUser(user);

        validationUtils.validateTrainer(trainer);

        trainerRepository.save(trainer);

        log.info("Trainee profile created successfully for {}", user.getUsername());

        return trainerDto;
    }

    public void changeTrainerPassword(String username, String newPassword) {
        log.info("Changing password for trainer {}", username);
        TrainerEntity trainer = trainerRepository.findByTrainerFromUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found"));

        UserEntity user = trainer.getUser();
        validationUtils.validatePasswordMatch(user, newPassword);
        user.setPassword(newPassword);
        userRepository.update(user);
        log.info("Password updated successfully for trainer {}", username);
    }

    public void toggleTrainerStatus(String username) {
        log.info("Toggling trainer status for {}", username);
        TrainerEntity trainer = trainerRepository.findByTrainerFromUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found"));

        validationUtils.validateActivateDeactivateTrainer(trainer);
        UserEntity user = trainer.getUser();
        user.setIsActive(! user.getIsActive());
        userRepository.update(user);
        log.info("Trainer status toggled successfully for {}", username);
    }
    // 9. Update Trainer Profile
    @Transactional
    public void updateTrainerProfile(String username, TrainerDto trainerDto) {
        log.info("Updating trainer profile for {}", username);
        TrainerEntity trainer = trainerRepository.findByTrainerFromUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found with username: "
                        + username));

        validationUtils.validateUpdateTrainer(trainer);
        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setTrainingTypeName(trainerDto.getSpecialization().getTrainingTypeName());
        trainer.setSpecialization(trainingTypeEntity);
        trainerRepository.update(trainer);

        log.info("Trainer profile updated successfully for {}", username);
    }

    // 14. Get Trainer Trainings List by Trainer Username and Criteria
    public List<TrainingEntity> getTrainerTrainings(String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        log.info("Fetching trainings for trainer: {}", trainerUsername);
        TrainerEntity trainer = trainerRepository.findByTrainerFromUsername(trainerUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found with username: "
                        + trainerUsername));
        validationUtils.validateTrainerTrainingsCriteria(trainerUsername, fromDate, toDate, traineeName);

        // Assuming TrainingRepository has a method for filtering by date range and trainee name
        List<TrainingEntity> trainings = trainingRepository.findTrainingsForTrainer(
                        trainer.getId(), fromDate, toDate, traineeName)
                .orElseThrow(() -> new ResourceNotFoundException("Trainings not found"));

        log.info("Found {} trainings for trainer: {}", trainings.size(), trainerUsername);
        return trainings;
    }

}
