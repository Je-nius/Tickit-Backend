package jenius.performanceservice.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jenius.performanceservice.domain.Performance;
import jenius.performanceservice.domain.PerformanceGenre;
import jenius.performanceservice.domain.QPerformance;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PerformanceRepository {

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    public PerformanceRepository(EntityManager em) {
        this.em = em;
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    public Performance save(Performance performance) {
        em.persist(performance);
        return performance;
    }

    public Optional<Performance> findById(Long performanceId) {
        return Optional.ofNullable(em.find(Performance.class, performanceId));
    }

    public List<Performance> findPerformance(String keyword) {
        QPerformance performance = QPerformance.performance;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            booleanBuilder.and(performance.title.containsIgnoreCase(keyword))
                    .or(performance.location.containsIgnoreCase(keyword))
                    .or(performance.artists.containsIgnoreCase(keyword));
        }

        return jpaQueryFactory
                .selectFrom(performance)
                .where(booleanBuilder)
                .fetch();
    }

    public List<Performance> findPerformanceByGenre(PerformanceGenre genre) {
        QPerformance performance = QPerformance.performance;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (genre != null) {
            booleanBuilder.and(performance.genre.eq(genre));
        }

        return jpaQueryFactory
                .selectFrom(performance)
                .where(booleanBuilder)
                .fetch();
    }

    public void deleteById(Long performanceId) {
        Performance performance = em.find(Performance.class, performanceId);
        em.remove(performance);
    }

}
