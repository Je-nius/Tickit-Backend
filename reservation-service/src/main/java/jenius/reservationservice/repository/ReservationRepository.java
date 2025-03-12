package jenius.reservationservice.repository;

import jakarta.persistence.EntityManager;
import jenius.reservationservice.domain.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {

    private final EntityManager em;

    public void save(Reservation reservation) {
        em.persist(reservation);
    }

    public Optional<Reservation> findReservationById(Long reservationId) {
        return Optional.ofNullable(em.find(Reservation.class, reservationId));
    }

    public List<Reservation> findByUserId(Long userId) {
        return em.createQuery("SELECT r FROM Reservation r WHERE r.userId = :userId", Reservation.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public boolean existByRsvNumber(String rsvNumber) {
        return em.createQuery("SELECT count(r) FROM Reservation r WHERE r.reservationNumber = :rsvNumber", Long.class)
                .setParameter("rsvNumber", rsvNumber)
                .getSingleResult() > 0;

    }



}
