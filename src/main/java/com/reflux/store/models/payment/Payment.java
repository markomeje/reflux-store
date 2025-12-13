package com.reflux.store.models.payment;
import com.reflux.store.models.order.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne(mappedBy = "payment",  cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Order order;

    @NotBlank
    @Size(min = 4, max = 6)
    private String paymentMethod;
    private String gatewayPaymentId;
    private String gatewayStatus;
    private String gatewayResponseMessage;
    private String gatewayName;

    public Payment(
        String paymentMethod,
        String gatewayPaymentId,
        String gatewayStatus,
        String gatewayResponseMessage,
        String gatewayName
    ) {
        this.paymentMethod = paymentMethod;
        this.gatewayPaymentId = gatewayPaymentId;
        this.gatewayStatus = gatewayStatus;
        this.gatewayResponseMessage = gatewayResponseMessage;
        this.gatewayName = gatewayName;
    }
}
