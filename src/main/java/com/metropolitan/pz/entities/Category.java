package com.metropolitan.pz.entities;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "categories")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // npr. "Hrana", "Lekovi", "Oprema"
}