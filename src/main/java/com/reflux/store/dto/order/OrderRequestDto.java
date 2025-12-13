package com.reflux.store.dto.order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private Long addressId;
    private String paymentMethod;
    private String gatewayPaymentId;
    private String gatewayStatus;
    private String gatewayResponseMessage;
    private String gatewayName;
}
