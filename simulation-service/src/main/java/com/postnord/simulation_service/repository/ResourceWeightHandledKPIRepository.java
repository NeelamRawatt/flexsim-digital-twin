package com.postnord.simulation_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postnord.simulation_service.entity.ResourceWeightHandledKPI;

import java.util.List;

@Repository
public interface ResourceWeightHandledKPIRepository extends JpaRepository<ResourceWeightHandledKPI,Long>
{


    List<ResourceWeightHandledKPI> findBySimExpId(Integer simExpId);
}
