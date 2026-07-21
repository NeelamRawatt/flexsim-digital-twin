package com.postnord.simulation_service.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postnord.simulation_service.entity.InfeedParcelsUnloadedKPIPerTick;

import java.util.List;

@Repository
public interface InfeedParcelsUnloadedKPIPerTickRepository extends JpaRepository<InfeedParcelsUnloadedKPIPerTick,Long> {

    List<InfeedParcelsUnloadedKPIPerTick> findBySimExpId(Integer simExpId);
}
