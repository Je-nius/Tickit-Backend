package jenius.performanceservice.repository;

import jenius.performanceservice.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    @Query("SELECT p " +
            "FROM Performance p " +
            "WHERE LOWER(p.title) LIKE CONCAT('%', :keyword, '%') OR LOWER(p.location) LIKE CONCAT('%', :keyword, '%')")
    List<Performance> findByTitleOrLocation(@Param(value = "keyword") String keyword);


}
