package jenius.reservationservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jenius.seatservice.domain.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCreateRequestDto {

    @NotNull
    private Long performanceId;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate performanceDate;
    @NotNull
    private int quantity;
    @NotNull
    private SeatType seatType;
//    @NotNull
//    private String payType;

//    private String entryToken;

}
