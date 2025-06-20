package com.product_service.product_service.model;

import lombok.Data;

@Data
public class Product {
    private int id;
    private String name;
    private double price;
    private String description;
    private int stock; 
}

