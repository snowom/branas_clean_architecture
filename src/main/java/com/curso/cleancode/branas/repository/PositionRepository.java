package com.curso.cleancode.branas.repository;

import com.curso.cleancode.branas.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, String> {
    List<Optional<Position>> findByRideId(String rideId);
}
