package junkyard.payment.infrastructure.payment.order;

import junkyard.payment.domain.order.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    Optional<PaymentOrder> readByOrderId(String orderId);
}
