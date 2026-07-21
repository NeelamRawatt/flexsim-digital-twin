package com.postnord.simulation_service.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postnord.simulation_service.entity.ChuteInsightsKPIPerTick;

@Repository
public interface ChuteInsightsKPIPerTickRepository extends JpaRepository<ChuteInsightsKPIPerTick, Long> {

List<ChuteInsightsKPIPerTick> findBySimExpId(Integer simExpId);


}



