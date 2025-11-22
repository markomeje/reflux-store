package com.reflux.store.dto.cart;
import com.reflux.store.dto.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long id;
    private ProductDto productDto;
    private Double productPrice;
    private Integer quantity;
    private CartDto cartDto;
    private Double discount;

}
