package com.postnord.simulation_service.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.postnord.simulation_service.entity.InfeedResourceDetail;

import java.util.List;

public interface InfeedResourceDetailRepository extends JpaRepository<InfeedResourceDetail, Long> {
    List<InfeedResourceDetail> findByExperimentId(Long experimentId, Sort sort);
}