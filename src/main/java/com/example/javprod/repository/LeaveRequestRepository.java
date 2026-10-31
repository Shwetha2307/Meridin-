package com.example.javprod.repository;

import com.example.javprod.domain.LeaveRequest;
import com.example.javprod.domain.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    @Query("SELECT l FROM LeaveRequest l JOIN FETCH l.employee e JOIN FETCH e.department ORDER BY l.appliedAt DESC")
    List<LeaveRequest> findAllWithDetails();

    @Query("SELECT l FROM LeaveRequest l JOIN FETCH l.employee e JOIN FETCH e.department WHERE l.status = :status ORDER BY l.appliedAt DESC")
    List<LeaveRequest> findByStatusWithDetails(LeaveStatus status);

    @Query("SELECT l FROM LeaveRequest l JOIN FETCH l.employee e JOIN FETCH e.department WHERE e.id = :employeeId ORDER BY l.appliedAt DESC")
    List<LeaveRequest> findByEmployeeIdWithDetails(Long employeeId);

    long countByStatus(LeaveStatus status);

    @Query("SELECT COUNT(l) > 0 FROM LeaveRequest l WHERE l.employee.id = :employeeId " +
           "AND l.status <> com.example.javprod.domain.LeaveStatus.REJECTED " +
           "AND l.startDate <= :end AND l.endDate >= :start")
    boolean existsOverlappingLeave(Long employeeId, java.time.LocalDate start, java.time.LocalDate end);
}
