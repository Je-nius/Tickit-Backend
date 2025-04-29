package jenius.seatservice.repository;

import jenius.seatservice.domain.Seat;
import jenius.seatservice.domain.SeatType;
import jenius.seatservice.dto.request.SeatSearchDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    boolean existsByPerformanceScheduleId(Long performanceScheduleId);
    Optional<Seat> findFirstByPerformanceScheduleIdAndSeatType(Long performanceScheduleId, SeatType seatType);
    @Query("SELECT new jenius.seatservice.dto.request.SeatSearchDto(s.zone, s.seatType, s.price, COUNT(s)) " +
            "FROM Seat s " +
            "WHERE s.performanceScheduleId = :performanceScheduleId " +
            "GROUP BY s.zone, s.seatType, s.price")
    List<SeatSearchDto> findByPerformanceScheduleId(@Param("performanceScheduleId") Long performanceScheduleId);
    Long countByPerformanceScheduleId(Long performanceScheduleId);
}
