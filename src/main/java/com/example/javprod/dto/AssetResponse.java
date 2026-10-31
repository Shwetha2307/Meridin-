package com.example.javprod.dto;

import com.example.javprod.domain.Asset;
import com.example.javprod.domain.AssetCategory;
import com.example.javprod.domain.AssetStatus;

import java.time.LocalDate;

public record AssetResponse(
    Long id,
    String assetTag,
    String name,
    AssetCategory category,
    String serialNumber,
    LocalDate assignedDate,
    AssetStatus status,
    Long assignedEmployeeId,
    String assignedEmployeeName,
    String assignedEmployeeDepartment
) {
    public static AssetResponse fromEntity(Asset asset) {
        if (asset == null) return null;
        Long empId = asset.getAssignedEmployee() != null ? asset.getAssignedEmployee().getId() : null;
        String empName = asset.getAssignedEmployee() != null 
            ? asset.getAssignedEmployee().getFirstName() + " " + asset.getAssignedEmployee().getLastName()
            : null;
        String deptName = (asset.getAssignedEmployee() != null && asset.getAssignedEmployee().getDepartment() != null)
            ? asset.getAssignedEmployee().getDepartment().getName()
            : null;

        return new AssetResponse(
            asset.getId(),
            asset.getAssetTag(),
            asset.getName(),
            asset.getCategory(),
            asset.getSerialNumber(),
            asset.getAssignedDate(),
            asset.getStatus(),
            empId,
            empName,
            deptName
        );
    }
}
