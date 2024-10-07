-- Create table for UserEntity (base class)
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       is_active BOOLEAN NOT NULL
);

-- Create table for TraineeEntity (inherits from UserEntity)
CREATE TABLE trainee (
                         id BIGINT PRIMARY KEY,
                         date_of_birth TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         address VARCHAR(255),
                         CONSTRAINT fk_user_trainee FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create table for TrainerEntity (inherits from UserEntity)
CREATE TABLE trainer (
                         id BIGINT PRIMARY KEY,
                         specialization_id BIGINT,
                         CONSTRAINT fk_user_trainer FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE,
                         CONSTRAINT fk_specialization FOREIGN KEY (specialization_id) REFERENCES training_types(id) ON DELETE SET NULL
);

-- Create table for TrainingTypeEntity
CREATE TABLE training_types (
                                id BIGSERIAL PRIMARY KEY,
                                training_type_name VARCHAR(255) NOT NULL
);

-- Create table for TrainingEntity
CREATE TABLE training (
                          id BIGSERIAL PRIMARY KEY,
                          trainee_id BIGINT NOT NULL,
                          trainer_id BIGINT NOT NULL,
                          training_name VARCHAR(255) NOT NULL,
                          training_type_id BIGINT,
                          training_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          training_duration INTEGER NOT NULL,
                          CONSTRAINT fk_trainee FOREIGN KEY (trainee_id) REFERENCES trainee(id) ON DELETE CASCADE,
                          CONSTRAINT fk_trainer FOREIGN KEY (trainer_id) REFERENCES trainer(id) ON DELETE CASCADE,
                          CONSTRAINT fk_training_type FOREIGN KEY (training_type_id) REFERENCES training_types(id) ON DELETE SET NULL
);

-- Create many-to-many relationship between TraineeEntity and TrainerEntity
CREATE TABLE trainee_trainer (
                                 trainee_id BIGINT NOT NULL,
                                 trainer_id BIGINT NOT NULL,
                                 PRIMARY KEY (trainee_id, trainer_id),
                                 CONSTRAINT fk_trainee_trainer FOREIGN KEY (trainee_id) REFERENCES trainee(id) ON DELETE CASCADE,
                                 CONSTRAINT fk_trainer_trainee FOREIGN KEY (trainer_id) REFERENCES trainer(id) ON DELETE CASCADE
);
