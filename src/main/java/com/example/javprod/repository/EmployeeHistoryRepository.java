package com.example.javprod.repository;

import com.example.javprod.domain.EmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {

    @Query("SELECT h FROM EmployeeHistory h JOIN FETCH h.employee e ORDER BY h.timestamp DESC LIMIT 20")
    List<EmployeeHistory> findRecentActivities();

    @Query("SELECT h FROM EmployeeHistory h JOIN FETCH h.employee e WHERE e.id = :employeeId ORDER BY h.timestamp DESC")
    List<EmployeeHistory> findByEmployeeId(Long employeeId);
}
