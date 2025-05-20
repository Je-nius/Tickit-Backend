package jenius.reservationservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jenius.seatservice.dto.request.SeatInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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
    @NotNull @Valid
    private List<SeatInfoDto> seatInfos;
    @NotNull
    private Long totalAmount;
//    @NotNull
//    private String payType;

//    private String entryToken;

}
