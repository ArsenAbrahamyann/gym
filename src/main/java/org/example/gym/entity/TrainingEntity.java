package org.example.gym.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "training")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trainee_id", referencedColumnName = "id")
    private TraineeEntity trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id", referencedColumnName = "id")
    private TrainerEntity trainer;

    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "training_type_id", referencedColumnName = "id")
    private TrainingTypeEntity trainingType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "training_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime trainingDate;

    @Column(name = "training_duration", nullable = false)
    private Integer trainingDuration;
}
