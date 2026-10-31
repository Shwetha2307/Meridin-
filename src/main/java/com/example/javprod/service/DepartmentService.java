package com.example.javprod.service;

import com.example.javprod.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {
    List<DepartmentDTO> getAllDepartments();
    DepartmentDTO getDepartmentById(Long id);
    DepartmentDTO createDepartment(DepartmentDTO dto);
    DepartmentDTO updateDepartment(Long id, DepartmentDTO dto);
    void deleteDepartment(Long id);
}
