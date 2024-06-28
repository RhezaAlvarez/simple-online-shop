package com.example.btpn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.btpn.models.Orders;

@Repository
public interface OrdersRepo extends JpaRepository<Orders, Integer>, JpaSpecificationExecutor<Orders>{
    
}
