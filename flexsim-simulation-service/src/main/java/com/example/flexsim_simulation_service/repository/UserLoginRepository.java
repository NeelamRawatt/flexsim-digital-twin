package com.example.flexsim_simulation_service.repository;


import com.example.flexsim_simulation_service.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserLoginRepository  extends JpaRepository<User,Long>{

   User findByUsername(String username);
    
}
