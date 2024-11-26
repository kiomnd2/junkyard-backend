package junkyard.payment.domain.order;

import jakarta.persistence.*;
import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.payment.PaymentAlreadyProcessedException;
import junkyard.payment.domain.PSPConfirmationStatus;
import junkyard.payment.domain.PaymentEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "payment_orders")
public class PaymentOrder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentEvent paymentEvent;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_order_status")
    private PaymentOrderStatus paymentOrderStatus;

    @Column(name = "ledger_updated")
    private Boolean ledgerUpdated;

    @Column(name = "wallet_updated")
    private Boolean walletUpdated;

    @Column(name = "failed_count", nullable = false)
    private Long failedCount;

    @Column(name = "threshold", nullable = false)
    private Long threshold;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void setPaymentEvent(PaymentEvent paymentEvent) {
        this.paymentEvent = paymentEvent;
    }



    @Getter
    @RequiredArgsConstructor
    public enum PaymentOrderStatus {
        NOT_STARTED(" 결제 시작 전"),
        EXECUTING("결제 중"),
        SUCCESS("결제 승인 완료"),
        FAILURE("결제 승인 실패"),
        UNKNOWN("결제 승인 할 수 없는 상태");

        private final String description;

        public static PaymentOrderStatus get(final String type) {
            PaymentOrderStatus[] values = values();
            for (PaymentOrderStatus value : values) {
                if (type.equals(value.name())) {
                    return value;
                }
            }
            throw new IllegalArgumentException("해당 type 은 잘못된 타입입니다 " + type);
        }
    }

    @Builder
    public PaymentOrder(PaymentEvent paymentEvent, Long sellerId, String productId,
                        String orderId, BigDecimal amount, PaymentOrderStatus paymentOrderStatus) {
        this.paymentEvent = paymentEvent;
        this.sellerId = sellerId;
        this.productId = productId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentOrderStatus = paymentOrderStatus;
        this.ledgerUpdated = false;
        this.walletUpdated = false;
        this.failedCount = 0L;
        this.threshold = 5L;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void changeOrderStatusToExecuting() {
        switch (this.paymentOrderStatus) {
            case SUCCESS -> throw new PaymentAlreadyProcessedException(Codes.PAYMENT_STATUS_SUCCESS_ERROR, paymentOrderStatus.name());
            case FAILURE -> throw new PaymentAlreadyProcessedException(Codes.PAYMENT_STATUS_FAILED_ERROR, paymentOrderStatus.name());
        }
        this.paymentOrderStatus = PaymentOrderStatus.EXECUTING;
    }

    public void changeUpdateStatus(PaymentOrderStatus status) {
        if (status == PaymentOrderStatus.UNKNOWN) this.failedCount++;
        this.paymentOrderStatus = status;
    }

}
