package com.reflux.store.controllers;
import com.reflux.store.dto.order.OrderDto;
import com.reflux.store.dto.order.OrderRequestDto;
import com.reflux.store.security.AuthSecurity;
import com.reflux.store.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    AuthSecurity authSecurity;
    OrderService orderService;

    public ResponseEntity<OrderDto> getOrderById(Long id) {
        return ResponseEntity.ok().body(new OrderDto());
    }

    @PostMapping("/initiate/{paymentMethod}")
    public ResponseEntity<OrderDto> orderProducts(
        @PathVariable String paymentMethod,
        @RequestBody OrderRequestDto orderRequestDto
    ) {
        String email = authSecurity.getLoggedInEmail();
        OrderDto orderDto = orderService.placeOrder(
            email,
            paymentMethod,
            orderRequestDto.getAddressId(),
            orderRequestDto.getGatewayName(),
            orderRequestDto.getGatewayPaymentId(),
            orderRequestDto.getGatewayStatus(),
            orderRequestDto.getGatewayResponseMessage()
        );

        return new ResponseEntity<>(orderDto,HttpStatus.CREATED);
    }
}
