package com.reflux.store.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private String paymentMethod;
    private String gatewayPaymentId;
    private String gatewayStatus;
    private String gatewayResponseMessage;
    private String gatewayName;
}
