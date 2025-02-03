package org.example.gym.repository;

import org.example.gym.dto.request.TrainerWorkloadRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Feign client for interacting with the trainer workload service.
 * This interface facilitates the remote calling of the service dedicated to handling
 * trainer workload operations, abstracting the complexity of HTTP communications.
 *
 * @FeignClient Configures this interface as a Feign client with a specified name and URL.
 *     The {@code fallback} attribute specifies the class that should handle the case where the
 *     service is not reachable or returns an error.
 */
@FeignClient(name = "spring-cloud-trainerWorkloadService",
        url = "http://trainer-service:8081/trainer-workload")
public interface TrainerWorkloadClient {

    /**
     * Updates the workload for a trainer based on the provided request data.
     * This endpoint is intended to receive workload updates which might include changes
     * in schedules, assigned tasks, or other metrics relevant to trainer management.
     *
     * @param token Authorization token that is required to validate the request
     *              to ensure that the operation is performed by an authenticated and authorized user.
     * @param request The {@link TrainerWorkloadRequest} object encapsulating the data necessary
     *                for updating a trainer's workload information. It should include all
     *                relevant details such as trainer identity, period of workload, and specifics
     *                of the tasks.
     * @return a {@link ResponseEntity} that contains the result as a String. The response entity
     *         will typically encompass confirmation of the update operation's success or failure
     *         description.
     */
    @PostMapping("/update")
    ResponseEntity<String> updateWorkload(@RequestHeader("Authorization") String token,
                                          @RequestBody TrainerWorkloadRequest request);
}
