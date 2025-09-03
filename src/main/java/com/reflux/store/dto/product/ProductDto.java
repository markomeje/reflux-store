package com.reflux.store.dto.product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String  name;
    private String  description;
    private double  price;
    private double  discount;
    private double  specialPrice;
    private Integer  quantity;
    private String image;
}
