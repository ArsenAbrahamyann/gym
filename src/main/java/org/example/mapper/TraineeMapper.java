package org.example.mapper;

import org.example.entity.TraineeEntity;
import org.example.paylod.request.TraineeRegistrationRequestDto;
import org.springframework.stereotype.Component;

@Component
public class TraineeMapper {

    public TraineeEntity traineeRegistrationMapToEntity(TraineeRegistrationRequestDto requestDto) {
        TraineeEntity trainee = new TraineeEntity();

    }
}
