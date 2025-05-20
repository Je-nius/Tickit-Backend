package jenius.payservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    private String tid;
    private String orderId;
    private String userId;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public void approve() {
        this.status = PaymentStatus.APPROVED;
    }

    public void cancel() {
        this.status = PaymentStatus.CANCEL;
    }

    public void fail() {
        this.status = PaymentStatus.FAIL;
    }
}
