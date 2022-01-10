package com.example.ticketmicroservice.VO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CarProduct {

    private Long carId;

    private String year;
    private String titleStatus;
    private Integer mileage;
    private String color;

    private String vinNumber;
    private Integer lot;

    private String state;
    private String country;
    private String condition;

    private Long minPrice;

    private Long maxPrice;


}
