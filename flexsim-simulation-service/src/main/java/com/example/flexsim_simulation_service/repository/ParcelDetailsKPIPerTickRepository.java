package com.example.flexsim_simulation_service.repository;

import com.example.flexsim_simulation_service.entity.ParcelDetailsKPIPerTick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Collection;

@Repository
public interface ParcelDetailsKPIPerTickRepository extends JpaRepository<ParcelDetailsKPIPerTick,Long> {


    List<ParcelDetailsKPIPerTick> findBySimExpId(Integer simExpId);
}
