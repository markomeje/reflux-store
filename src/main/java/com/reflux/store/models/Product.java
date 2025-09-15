package com.reflux.store.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 3, message = "Product name must contain at least three characters")
    private String name;

    @NotBlank(message = "Product description cannot be blank")
    @Size(min = 7, message = "Product description must contain at least seven characters")
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
