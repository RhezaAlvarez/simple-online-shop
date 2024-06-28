package com.example.btpn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.btpn.models.Customers;
import java.util.Optional;


@Repository
public interface CustomersRepo extends JpaRepository<Customers, Integer>, JpaSpecificationExecutor<Customers>{
    Optional<Customers> findByCustomerName(String customerName);
}
