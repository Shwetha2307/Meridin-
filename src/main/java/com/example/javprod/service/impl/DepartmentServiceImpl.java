package com.example.javprod.service.impl;

import com.example.javprod.domain.Department;
import com.example.javprod.dto.DepartmentDTO;
import com.example.javprod.exception.DuplicateResourceException;
import com.example.javprod.exception.InvalidOperationException;
import com.example.javprod.exception.ResourceNotFoundException;
import com.example.javprod.repository.DepartmentRepository;
import com.example.javprod.repository.EmployeeRepository;
import com.example.javprod.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
            .map(dept -> new DepartmentDTO(
                dept.getId(),
                dept.getName(),
                dept.getCode(),
                dept.getDescription(),
                employeeRepository.countByDepartmentId(dept.getId())
            ))
            .toList();
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id) {
        Department dept = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        long count = employeeRepository.countByDepartmentId(id);
        return new DepartmentDTO(dept.getId(), dept.getName(), dept.getCode(), dept.getDescription(), count);
    }

    @Override
    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO dto) {
        if (departmentRepository.existsByCode(dto.code().toUpperCase().trim())) {
            throw new DuplicateResourceException("Department code already exists: " + dto.code());
        }

        Department dept = Department.builder()
            .name(dto.name().trim())
            .code(dto.code().toUpperCase().trim())
            .description(dto.description() != null ? dto.description().trim() : null)
            .build();

        Department saved = departmentRepository.save(dept);
        return new DepartmentDTO(saved.getId(), saved.getName(), saved.getCode(), saved.getDescription(), 0);
    }

    @Override
    @Transactional
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO dto) {
        Department dept = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        String upperCode = dto.code().toUpperCase().trim();
        if (departmentRepository.existsByCodeAndIdNot(upperCode, id)) {
            throw new DuplicateResourceException("Department code already in use by another department: " + dto.code());
        }

        dept.setName(dto.name().trim());
        dept.setCode(upperCode);
        dept.setDescription(dto.description() != null ? dto.description().trim() : null);

        Department updated = departmentRepository.save(dept);
        long count = employeeRepository.countByDepartmentId(id);
        return new DepartmentDTO(updated.getId(), updated.getName(), updated.getCode(), updated.getDescription(), count);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        Department dept = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        long employeeCount = employeeRepository.countByDepartmentId(id);
        if (employeeCount > 0) {
            throw new InvalidOperationException("Cannot delete department '" + dept.getName() + "' because " + employeeCount + " employee(s) are assigned to it.");
        }

        departmentRepository.delete(dept);
    }
}
