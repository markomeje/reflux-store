package com.reflux.store.dto.order;
import com.reflux.store.models.Product;
import com.reflux.store.models.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long id;
    private String description;
    private Integer quantity;
    private Double discount;
    private Double productPrice;
    private Order order;
    private Product product;
}
