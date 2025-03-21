package jenius.tickitapi.reservation;

import io.swagger.v3.oas.annotations.Operation;
import jenius.reservationservice.dto.request.ReservationCreateRequestDto;
import jenius.reservationservice.dto.response.ReservationCreateResponseDto;
import jenius.reservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 결제 -> 예매 내역 확인 page
    @Operation(
            summary = "예매 API",
            description = "예매 완료 후 보여지는 예매 내역 페이지"
    )
    @PostMapping("/api/ticket/reservations")
    public ResponseEntity<ReservationCreateResponseDto> reserve(@RequestBody ReservationCreateRequestDto createRequestDto) {
        // TODO: userId 받아오기
        Long userId = 1L;
        return ResponseEntity.ok()
                .body(reservationService.reserve(userId, createRequestDto));
    }

}
