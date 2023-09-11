package com.curso.cleancode.branas.repository;

import com.curso.cleancode.branas.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, String> {
    @Query(value = "select count(status) from ride where passenger_id = ?1 AND status != ?2", nativeQuery = true)
    Integer countPassengerRidesWithStatusNotEquals(String accountId, String rideStatusEnum);
    Optional<Ride> findByRideId(String rideId);
    @Query(value = "select count(status) from ride where driver_id = ?1 AND status in(\"accepted\", \"in_progress\")", nativeQuery = true)
    Integer countDriverPendingRide(String driverId);
}
