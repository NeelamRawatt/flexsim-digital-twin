package com.example.flexsim_simulation_service.repository.experiment;

import com.example.flexsim_simulation_service.entity.experiment.Experiment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, Long> {
    Page<Experiment> findByUserUsernameIgnoreCase(String username, Pageable pageable);


}
