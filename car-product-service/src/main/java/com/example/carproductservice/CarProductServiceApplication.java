package com.example.carproductservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CarProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarProductServiceApplication.class, args);
	}

}

