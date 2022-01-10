package com.example.carproductservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "CustomerCar")
@Table(name = "Car",  uniqueConstraints = { @UniqueConstraint(name = "vin_number", columnNames = "vin_number")})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CustomerCar {
    @Id
    @SequenceGenerator(
            name="car_sequence", sequenceName = "car_sequence", allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_sequence")
    private Long carId;

    private String year;
    private String titleStatus;
    private Integer mileage;
    private String color;

    @Column(name = "vin_number", nullable = false)
    private String vinNumber;
    private Integer lot;

    private String state;
    private String country;
    private String condition;

    private Long minPrice;

    private Long maxPrice;


}
