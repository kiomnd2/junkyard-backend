package junkyard.domain.payment.order;

import jakarta.persistence.*;
import junkyard.domain.payment.PaymentEvent;
import junkyard.domain.payment.order.history.PaymentOrderHistory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Long productId;

    @Column(name = "order_id", unique = true)
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "paymentOrder", cascade = CascadeType.PERSIST)
    private List<PaymentOrderHistory> paymentOrderHistories;

    public void setPaymentEvent(PaymentEvent paymentEvent) {
        this.paymentEvent = paymentEvent;
    }

    public void addHistory(PaymentOrderHistory history) {
        this.paymentOrderHistories.add(history);
        history.setPaymentOrder(this);
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
    }

    @Builder
    public PaymentOrder(PaymentEvent paymentEvent, Long sellerId, Long productId,
                        String orderId, BigDecimal amount, PaymentOrderStatus paymentOrderStatus,
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.paymentOrderHistories = new ArrayList<>();
    }

}
