package com.metropolitan.pz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductUpsertDto {
    public String name;
    public String image;
    public Long categoryId;
    public String material;
    public Double price;
    public String description;
    public Integer stock;
}