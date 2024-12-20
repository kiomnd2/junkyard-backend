package junkyard.payment.domain;

import jakarta.persistence.*;
import junkyard.payment.domain.confirm.PaymentStatusUpdateCommand;
import junkyard.payment.domain.order.PaymentOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "payment_events")
public class PaymentEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Column(name = "is_payment_done", nullable = false)
    private Boolean isPaymentDone;

    @Column(name = "payment_key", unique = true)
    private String paymentKey;

    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @Enumerated(EnumType.STRING)
    private PaymentType type;

    @Column(name = "order_name", nullable = false)
    private String orderName;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;
    @Column(name = "psp_raw_data")
    private String pspRawData;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "paymentEvent", cascade = CascadeType.PERSIST)
    private List<PaymentOrder> paymentOrders;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void addPaymentOrder(PaymentOrder paymentOrder) {
        this.paymentOrders.add(paymentOrder);
        paymentOrder.setPaymentEvent(this);
    }

    public void updatePaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }


    @RequiredArgsConstructor
    public enum PaymentType {
        NORMAL("일반 결제");

        private final String description;

        public static PaymentType get(final String type) {
            PaymentType[] values = values();
            for (PaymentType value : values) {
                if (type.equals(value.name())) {
                    return value;
                }
            }
            throw new IllegalArgumentException("해당 type 은 잘못된 타입입니다 " + type);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum PaymentMethod {
        EASY_PAY("간편결제");

        private final String description;

        public static PaymentMethod get(final String type) {
            PaymentMethod[] values = values();
            for (PaymentMethod value : values) {
                if (type.equals(value.description)) {
                    return value;
                }
            }
            throw new IllegalArgumentException("해당 type 은 잘못된 타입입니다 " + type);
        }
    }

    @Builder
    public PaymentEvent(Long buyerId, String paymentKey, String orderId, String orderName,
                        PaymentMethod method, String pspRawData, LocalDateTime approvedAt,
                        LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.buyerId = buyerId;
        this.isPaymentDone = false;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.type = PaymentType.NORMAL;;
        this.orderName = orderName;
        this.method = method;
        this.pspRawData = pspRawData;
        this.approvedAt = approvedAt;
        this.createdAt = createdAt;
        this.paymentOrders = new ArrayList<>();;
        this.updatedAt = updatedAt;
    }

    public Long totalAmount() {
        return this.paymentOrders.stream().mapToLong(order -> order.getAmount().longValue()).sum();
    }

    public void changePaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }

    public boolean isSuccess() {
        return paymentOrders.stream().allMatch(paymentOrder ->
                paymentOrder.getPaymentOrderStatus() == PaymentOrder.PaymentOrderStatus.SUCCESS);
    }

    public boolean isFailure() {
        return paymentOrders.stream().allMatch(paymentOrder ->
                paymentOrder.getPaymentOrderStatus() == PaymentOrder.PaymentOrderStatus.FAILURE);
    }

    public boolean isUnknown() {
        return paymentOrders.stream().allMatch(paymentOrder ->
                paymentOrder.getPaymentOrderStatus() == PaymentOrder.PaymentOrderStatus.UNKNOWN);
    }

    public void updateExtraDetail(PaymentStatusUpdateCommand command) {
        this.orderName = command.getPaymentExtraDetails().getOrderName();
        this.method = command.getPaymentExtraDetails().getPaymentMethod();;
        this.approvedAt = command.getPaymentExtraDetails().getApprovedAt();
        this.type = command.getPaymentExtraDetails().getPaymentType();
        this.orderId = command.getOrderId();
    }

}
