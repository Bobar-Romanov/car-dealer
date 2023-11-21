package com.service.carDealer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCard {
    private Long id;
    private String name;
    private String description;
    private Integer horsepower;
    private String wdType;
    private String boxType;
    private String color;
    private String date;
    private Integer price;
    private List<Option> options;


}
