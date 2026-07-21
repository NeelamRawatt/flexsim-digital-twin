package com.example.flexsim_simulation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.flexsim_simulation_service.entity.AcceptableParcelUnit;

@Repository
public interface AcceptableParcelUnitRepository extends JpaRepository<AcceptableParcelUnit,Long>{



    
}
