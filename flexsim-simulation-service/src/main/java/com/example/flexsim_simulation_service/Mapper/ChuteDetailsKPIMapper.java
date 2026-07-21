package com.example.flexsim_simulation_service.Mapper;


import com.example.flexsim_simulation_service.DTO.ChuteDetailsKPIDTO;
import com.example.flexsim_simulation_service.entity.ChuteDetailsKPI;
import org.springframework.stereotype.Component;

@Component
public class ChuteDetailsKPIMapper {



   public ChuteDetailsKPI fromDTO(ChuteDetailsKPIDTO dto)
   {
       return ChuteDetailsKPI.builder()
               .simExpId(dto.getSim_exp_id())
               .chute_id(dto.getChute_id())
               .type(dto.getType())
               .containers_filled(dto.getContainers_filled())
               .parcel_filled_in_cage(dto.getParcel_filled_in_cage())
               .parcel_throughput(dto.getParcel_throughput())
               .parcel_blocked(dto.getParcel_blocked())
               .parcel_handled_resource(dto.getParcel_handled_resource())
               .build();


   }

   public ChuteDetailsKPIDTO toDto(ChuteDetailsKPI entity)
   {
       return ChuteDetailsKPIDTO.builder()
               .sim_exp_id(entity.getSimExpId())
               .chute_id(entity.getChute_id())
               .type(entity.getType())
               .containers_filled(entity.getContainers_filled())
               .parcel_filled_in_cage(entity.getParcel_filled_in_cage())
               .parcel_throughput(entity.getParcel_throughput())
               .parcel_blocked(entity.getParcel_blocked())
               .parcel_handled_resource(entity.getParcel_handled_resource())
               .build();

   }



}
