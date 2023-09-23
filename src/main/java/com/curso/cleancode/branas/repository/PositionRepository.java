package com.curso.cleancode.branas.repository;

import com.curso.cleancode.branas.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, String> {
}
