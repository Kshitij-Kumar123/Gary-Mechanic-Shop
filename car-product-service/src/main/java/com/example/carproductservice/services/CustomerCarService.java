package com.example.carproductservice.services;

import com.example.carproductservice.model.CustomerCar;
import com.example.carproductservice.repository.CustomerCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerCarService {
    @Autowired
    private CustomerCarRepository customerCarRepository;

    public List<CustomerCar> findAll() {
        return customerCarRepository.findAll();
    }

    public List<CustomerCar> saveAll(List<CustomerCar> listOfCars) {
        return customerCarRepository.saveAll(listOfCars);
    }
    public CustomerCar save(CustomerCar car) {
        return customerCarRepository.save(car);
    }


    public void delete(Long carId) {
        CustomerCar findCarToDelete = customerCarRepository.findById(carId).orElseThrow(() -> new IllegalStateException("car id does not exist to delete"));
        customerCarRepository.delete(findCarToDelete);
    }

    public CustomerCar findById(Long carId) {
        CustomerCar findCarById = customerCarRepository.findById(carId).orElseThrow(() -> new IllegalStateException("car id does not exist"));
        return findCarById;
    }
}
