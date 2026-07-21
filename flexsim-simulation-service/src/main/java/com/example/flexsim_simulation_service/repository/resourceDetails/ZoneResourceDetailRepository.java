package com.example.flexsim_simulation_service.repository.resourceDetails;

import com.example.flexsim_simulation_service.entity.resourceDetails.ZoneResourceDetail;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneResourceDetailRepository extends JpaRepository<ZoneResourceDetail, Long> {
    public List<ZoneResourceDetail> findByExperimentExperimentId(Long experimentId, Sort sort);
}
