package jenius.tickitapi.performance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jenius.performanceservice.dto.request.PerformanceCreateRequestDto;
import jenius.performanceservice.dto.response.PerformanceCreateResponseDto;
import jenius.performanceservice.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    @Operation(
            summary = "공연 등록 API",
            description ="공연을 등록할 수 있다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "공연 등록 성공"),
                    @ApiResponse(responseCode = "400", description = "공연 등록 실패")
            }
    )
    @PostMapping("/api/contents/create")
    public ResponseEntity<PerformanceCreateResponseDto> createPerformance(PerformanceCreateRequestDto
                                                                          createRequestDto) {
        PerformanceCreateResponseDto createResponseDto =
                performanceService.createPerformance(createRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createResponseDto);
    }


}
