package com.metropolitan.pz.entities;

import javax.persistence.*;
import lombok.*;

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

    //  umesto enum Category
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String material;

    @Column(nullable = false)
    private Double price;

    private String description;

    //  lager
    @Column(nullable = false)
    private Integer stock = 0; // default 0 da ne bude null
}
