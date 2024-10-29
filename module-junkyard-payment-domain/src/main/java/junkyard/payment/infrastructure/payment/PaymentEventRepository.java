package junkyard.payment.infrastructure.payment;

import junkyard.payment.domain.PaymentEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentEventRepository extends JpaRepository<PaymentEvent, Long> {
}
