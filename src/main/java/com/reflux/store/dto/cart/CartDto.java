package com.reflux.store.dto.cart;
import com.reflux.store.dto.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long id;
    private List<ProductDto> products = new ArrayList<>();
    private Double totalPrice = 0.0;
}
