package jenius.tickitapi.performance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jenius.performanceservice.dto.request.PerformanceCreateRequestDto;
import jenius.performanceservice.dto.request.PerformanceSearchRequestDto;
import jenius.performanceservice.dto.response.PerformanceCreateResponseDto;
import jenius.performanceservice.dto.response.PerformanceSearchResponseDto;
import jenius.performanceservice.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    @PostMapping(value = "/api/contents/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PerformanceCreateResponseDto> createPerformance(
            @RequestPart(value = "poster") MultipartFile multipartFile,
            @Valid @RequestPart PerformanceCreateRequestDto
                    createRequestDto) throws IOException {
        PerformanceCreateResponseDto createResponseDto =
                performanceService.createPerformance(multipartFile, createRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createResponseDto);
    }

    @PostMapping("/api/contents/search")
    public ResponseEntity<List<PerformanceSearchResponseDto>> searchPerformance(@RequestBody PerformanceSearchRequestDto
                                                                                  searchRequestDto) {
        List<PerformanceSearchResponseDto> searchResponseDtos =
                performanceService.searchPerformances(searchRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(searchResponseDtos);
    }


}
