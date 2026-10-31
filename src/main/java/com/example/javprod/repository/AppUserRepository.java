package com.example.javprod.repository;

import com.example.javprod.domain.AppUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    @EntityGraph(attributePaths = {"employee", "employee.department"})
    Optional<AppUser> findByUsernameIgnoreCase(String username);
}
