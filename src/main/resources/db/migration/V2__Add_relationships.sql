-- Create Join Table for Many-to-Many Relationship between Trainees and Trainers
CREATE TABLE IF NOT EXISTS trainee_trainer (
                                 trainee_id BIGINT NOT NULL,
                                 trainer_id BIGINT NOT NULL,
                                 PRIMARY KEY (trainee_id, trainer_id),
                                 FOREIGN KEY (trainee_id) REFERENCES trainee(id),
                                 FOREIGN KEY (trainer_id) REFERENCES trainer(id)
);
