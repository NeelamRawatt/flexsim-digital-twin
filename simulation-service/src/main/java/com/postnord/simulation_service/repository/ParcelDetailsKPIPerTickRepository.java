package com.postnord.simulation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postnord.simulation_service.entity.ParcelInsightsKPIPerTick;

import java.util.List;

import java.util.Collection;

@Repository
public interface ParcelDetailsKPIPerTickRepository extends JpaRepository<ParcelInsightsKPIPerTick,Long> {


    List<ParcelInsightsKPIPerTick> findBySimExpId(Integer simExpId);
}
