package com.example.flexsim_simulation_service.repository.resourceDetails;

import com.example.flexsim_simulation_service.entity.resourceDetails.InfeedResourceDetail;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfeedResourceDetailRepository extends JpaRepository<InfeedResourceDetail, Long> {
    public List<InfeedResourceDetail> findByExperimentExperimentId(Long experimentId, Sort sort);
}
