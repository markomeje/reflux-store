package com.reflux.store.dto.order;
import com.reflux.store.dto.PaymentDto;
import com.reflux.store.dto.address.AddressDto;
import com.reflux.store.models.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private String email;
    private List<OrderItemDto> orderItems;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String status;
    private AddressDto address;
    private PaymentDto payment;
    private Long addressId;
}
