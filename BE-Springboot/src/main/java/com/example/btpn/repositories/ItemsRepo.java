package com.example.btpn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.btpn.models.Items;
import java.util.Optional;


@Repository
public interface ItemsRepo extends JpaRepository<Items, Integer>, JpaSpecificationExecutor<Items>{
    Optional<Items> findByItemName(String itemName);
}
