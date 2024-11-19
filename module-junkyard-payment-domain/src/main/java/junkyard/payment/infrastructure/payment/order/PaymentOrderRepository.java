package junkyard.payment.infrastructure.payment.order;

import junkyard.payment.domain.order.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    List<PaymentOrder> readAllByOrderId(String orderId);
}
