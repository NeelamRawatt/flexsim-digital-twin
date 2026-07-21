package com.example.flexsim_simulation_service.repository;


import com.example.flexsim_simulation_service.DTO.InfeedParcelsUnloadedKPIPerTickDTO;
import com.example.flexsim_simulation_service.entity.InfeedParcelsUnloadedKPIPerTick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfeedParcelsUnloadedKPIPerTickRepository extends JpaRepository<InfeedParcelsUnloadedKPIPerTick,Long> {

    List<InfeedParcelsUnloadedKPIPerTick> findBySimExpId(Integer simExpId);
}
