package com.example.carproductservice.repository;

import com.example.carproductservice.model.CustomerCar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerCarRepository extends JpaRepository<CustomerCar, Long> {
}