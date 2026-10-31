package com.example.javprod.repository;

import com.example.javprod.domain.Employee;
import com.example.javprod.domain.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    long countByStatus(EmployeeStatus status);

    long countByHireDateAfter(LocalDate date);

    List<Employee> findByDepartmentId(Long departmentId);

    List<Employee> findByManagerId(Long managerId);

    long countByDepartmentId(Long departmentId);

    @Query("SELECT e FROM Employee e " +
           "LEFT JOIN FETCH e.department d " +
           "LEFT JOIN FETCH e.manager m " +
           "WHERE (:search IS NULL OR :search = '' OR " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.role) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:departmentId IS NULL OR d.id = :departmentId) " +
           "AND (:status IS NULL OR e.status = :status)")
    Page<Employee> searchEmployees(
        @Param("search") String search,
        @Param("departmentId") Long departmentId,
        @Param("status") EmployeeStatus status,
        Pageable pageable
    );
}
