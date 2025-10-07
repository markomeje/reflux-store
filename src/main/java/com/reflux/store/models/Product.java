package com.reflux.store.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@Entity
@Table(name = "products")
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

    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @DecimalMax(value = "999999.99", message = "Price cannot exceed 999999.99")
    private double price;

    @Column(nullable = false)
    @PositiveOrZero(message = "Product discount must be zero or greater")
    private double discount;

    @DecimalMin(value = "0.0", inclusive = false, message = "Special price must be greater than zero")
    @DecimalMax(value = "999999.99", message = "Special price cannot exceed 999999.99")
    private double specialPrice;

    @Column(nullable = false)
    @Min(value = 0, message = "Product quantity must be zero or greater")
    @Max(value = 1000, message = "Product quantity cannot exceed 1000")
    private Integer quantity;

    @Column(nullable = true)
    @NotBlank(message = "Product image cannot be blank")
    private String image;

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategory category;

}
