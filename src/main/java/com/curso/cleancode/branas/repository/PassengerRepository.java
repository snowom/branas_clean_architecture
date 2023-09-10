package com.curso.cleancode.branas.repository;

import com.curso.cleancode.branas.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, String> {
    public Optional<Passenger> findByEmail(String email);
    public Optional<Passenger> accountId(String accountId);
}
