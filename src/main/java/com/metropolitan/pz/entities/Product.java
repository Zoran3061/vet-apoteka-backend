package com.metropolitan.pz.entities;

import com.metropolitan.pz.entities.enums.Category;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    private String material;

    @Column(nullable = false)
    private Double price;

    private String description;

}

