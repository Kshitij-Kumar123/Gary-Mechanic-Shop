package com.example.carproductservice.controller;

import com.example.carproductservice.model.CustomerCar;
import com.example.carproductservice.services.CustomerCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/customer_car")
@Slf4j
public class CustomerCarController {

    @Autowired
    private CustomerCarService customerCarService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerCar> findAll() {
        return customerCarService.findAll();
    }

    @GetMapping("{carId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerCar findById(@PathVariable("carId") Long carId) {
        return customerCarService.findById(carId);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    public CustomerCar save(@RequestBody CustomerCar car) {
        return customerCarService.save(car);
    }


}
