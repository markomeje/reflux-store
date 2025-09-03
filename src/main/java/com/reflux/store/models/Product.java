package com.reflux.store.models;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    private String name;
    private String description;
    private double price;
    private double discount;
    private double specialPrice;
    private Integer quantity;
    private String image;

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;

}
