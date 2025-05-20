package jenius.seatservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jenius.seatservice.domain.SeatType;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SeatInfoDto {
    @Min(0)
    private int quantity;
    @NotNull
    private SeatType seatType;
}
