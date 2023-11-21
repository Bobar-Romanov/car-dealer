package com.service.carDealer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarModel {

    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Integer horsepower;
    private String wdType;
    private String boxType;

}
