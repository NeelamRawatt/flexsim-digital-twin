package com.example.flexsim_simulation_service.repository;


import com.example.flexsim_simulation_service.entity.ChuteDetailsKPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChuteDetailsKPIRepository extends JpaRepository<ChuteDetailsKPI,Integer> {

    List<ChuteDetailsKPI> findBySimExpId(Integer simExpId);
}
