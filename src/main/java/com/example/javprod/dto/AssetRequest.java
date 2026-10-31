package com.example.javprod.dto;

import com.example.javprod.domain.AssetCategory;
import com.example.javprod.domain.AssetStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AssetRequest(
    @NotBlank(message = "Asset tag is required")
    @Size(max = 30, message = "Asset tag cannot exceed 30 characters")
    String assetTag,

    @NotBlank(message = "Asset name is required")
    @Size(max = 100, message = "Asset name cannot exceed 100 characters")
    String name,

    @NotNull(message = "Category is required")
    AssetCategory category,

    @NotBlank(message = "Serial number is required")
    @Size(max = 100, message = "Serial number cannot exceed 100 characters")
    String serialNumber,

    @NotNull(message = "Status is required")
    AssetStatus status,

    Long assignedEmployeeId
) {}
