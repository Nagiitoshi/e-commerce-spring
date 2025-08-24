package com.nagi.e_commerce_spring.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.e_commerce_spring.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser_Id(Long userId);
    
}
