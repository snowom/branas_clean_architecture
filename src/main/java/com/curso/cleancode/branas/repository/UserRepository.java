package com.curso.cleancode.branas.repository;

import com.curso.cleancode.branas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    public Optional<User> findByEmail(String email);
    public Optional<User> findByAccountId(String accountId);
}
