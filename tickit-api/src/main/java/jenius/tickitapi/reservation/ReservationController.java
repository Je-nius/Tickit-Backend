package jenius.tickitapi.reservation;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jenius.reservationservice.dto.request.ReservationCreateRequestDto;
import jenius.reservationservice.dto.request.ReservationRequestDto;
import jenius.reservationservice.dto.response.ReservationCreateResponseDto;
import jenius.reservationservice.dto.response.ReservationResponseDto;
import jenius.reservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 대기열 -> 예매 페이지 입장
//    @Operation(
//            summary = "Enter RsvPage API",
//            description = "공연 타이틀, 시간, 장소, 좌석 타입 등의 정보를 보여주는 예매 페이지"
//    )
//    @GetMapping("/ticket/reservations/detail")
//    public ResponseEntity<ReservationResponseDto> enterReservation(@RequestBody ReservationRequestDto requestDto) {
//
//    }

    // 결제 -> 예매 내역 확인 page
    @Operation(
            summary = "예매 API",
            description = "예매 완료 후 보여지는 예매 내역 페이지"
    )
    @PostMapping("/ticket/reservations")
    public ResponseEntity<ReservationCreateResponseDto> reserve(@RequestBody @Valid ReservationCreateRequestDto createRequestDto) {
        // TODO: userId 받아오기
        Long userId = 1L;
        return ResponseEntity.ok()
                .body(reservationService.reserve(userId, createRequestDto));
    }

}
