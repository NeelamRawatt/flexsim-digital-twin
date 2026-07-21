package com.postnord.simulation_service.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.postnord.simulation_service.entity.ZoneResourceDetail;

import java.util.List;

public interface ZoneResourceDetailRepository extends JpaRepository<ZoneResourceDetail, Long> {
    List<ZoneResourceDetail> findByExperimentId(Long experimentId, Sort sort);
}
