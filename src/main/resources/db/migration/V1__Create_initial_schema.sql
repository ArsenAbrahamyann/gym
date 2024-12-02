
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       is_active BOOLEAN NOT NULL,
                       role VARCHAR(255) NOT NULL CHECK (role IN ('ROLE_TRAINEE', 'ROLE_TRAINER'))
);

CREATE TABLE training_types (
                                id SERIAL PRIMARY KEY,
                                training_type_name VARCHAR(255) NOT NULL
);

CREATE TABLE trainee (
                         id SERIAL PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         date_of_birth TIMESTAMP NOT NULL,
                         address VARCHAR(255),
                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE trainer (
                         id SERIAL PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         specialization_id BIGINT NOT NULL,
                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
                         FOREIGN KEY (specialization_id) REFERENCES training_types(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE training (
                          id SERIAL PRIMARY KEY,
                          trainee_id BIGINT NOT NULL,
                          trainer_id BIGINT NOT NULL,
                          training_name VARCHAR(255) NOT NULL,
                          training_type_id BIGINT NOT NULL,
                          training_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          training_duration INT NOT NULL,
                          FOREIGN KEY (trainee_id) REFERENCES trainee(id) ON DELETE CASCADE ON UPDATE CASCADE,
                          FOREIGN KEY (trainer_id) REFERENCES trainer(id) ON DELETE CASCADE ON UPDATE CASCADE,
                          FOREIGN KEY (training_type_id) REFERENCES training_types(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE trainee_trainer (
                                 trainee_id BIGINT NOT NULL,
                                 trainer_id BIGINT NOT NULL,
                                 PRIMARY KEY (trainee_id, trainer_id),
                                 FOREIGN KEY (trainee_id) REFERENCES trainee(id) ON DELETE CASCADE ON UPDATE CASCADE,
                                 FOREIGN KEY (trainer_id) REFERENCES trainer(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tokens (
                       id SERIAL PRIMARY KEY,
                       token VARCHAR(512) NOT NULL,
                       token_type VARCHAR(50) NOT NULL CHECK (token_type IN ('BEARER')),
                       revoked BOOLEAN NOT NULL DEFAULT FALSE,
                       user_id BIGINT NOT NULL,
                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);
