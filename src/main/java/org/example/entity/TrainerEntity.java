package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "trainer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "specialization_id", referencedColumnName = "id")
    private TrainingTypeEntity specialization;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;

    @ManyToMany(mappedBy = "trainers", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<TraineeEntity> trainees = new HashSet<>();

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private Set<TrainingEntity> training = new HashSet<>();

}
