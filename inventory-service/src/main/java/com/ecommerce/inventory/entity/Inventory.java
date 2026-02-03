package com.ecommerce.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "inventory", schema = "inventory_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private Long productId;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Boolean inStock;
}
