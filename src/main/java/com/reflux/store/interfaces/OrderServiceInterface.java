package com.reflux.store.interfaces;
import com.reflux.store.dto.order.OrderDto;
import jakarta.transaction.Transactional;

public interface OrderServiceInterface {
    @Transactional
    OrderDto placeOrder(
        String email,
        String paymentMethod,
        Long addressId,
        String gatewayName,
        String gatewayPaymentId,
        String gatewayStatus,
        String gatewayResponseMessage
    );
}
