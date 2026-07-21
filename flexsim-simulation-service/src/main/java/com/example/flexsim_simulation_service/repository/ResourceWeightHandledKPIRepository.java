package com.example.flexsim_simulation_service.repository;


import com.example.flexsim_simulation_service.entity.ResourceWeightHandledKPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceWeightHandledKPIRepository extends JpaRepository<ResourceWeightHandledKPI,Long>
{


    List<ResourceWeightHandledKPI> findBySimExpId(Integer simExpId);
}
