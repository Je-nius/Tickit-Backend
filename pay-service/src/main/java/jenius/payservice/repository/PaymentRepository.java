package jenius.payservice.repository;

import jenius.payservice.domain.Payment;
import jenius.payservice.domain.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findByOrderIdAndUserIdAndStatus(String orderId,
                                                      Long userId,
                                                      PaymentStatus status);
}
