package org.example.gym.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trainer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "specialization_id", referencedColumnName = "id")
    private TrainingTypeEntity specialization;

    @ManyToMany(mappedBy = "trainers", fetch = FetchType.EAGER)
    private Set<TraineeEntity> trainees = new HashSet<>();

    @OneToMany(mappedBy = "trainer", orphanRemoval = true)
    private Set<TrainingEntity> training = new HashSet<>();

}
