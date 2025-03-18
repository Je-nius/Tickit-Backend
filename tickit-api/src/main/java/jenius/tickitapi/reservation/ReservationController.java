package jenius.tickitapi.reservation;

import io.swagger.v3.oas.annotations.Operation;
import jenius.reservationservice.dto.request.ReservationCreateRequestDto;
import jenius.reservationservice.dto.response.ReservationCreateResponseDto;
import jenius.reservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 결제 -> 예매 내역 확인 page
//    @Operation(
//            summary = "예매 API",
//            description = "예매 완료 후 보여지는 예매 내역 페이지"
//    )
//    @GetMapping("/api/ticket/reservations")
//    public ResponseEntity<ReservationCreateResponseDto> reserve(ReservationCreateRequestDto createRequestDto) {
//        // TODO: userId 받아오기
//        Long userId = 1L;
//        reservationService.createReservation(userId, createRequestDto);
//    }

}
