package com.example.javprod.dto;

import java.util.List;
import java.util.Map;

public record AnalyticsSummaryDTO(
    long totalHeadcount,
    long activeHeadcount,
    long onLeaveHeadcount,
    long terminatedHeadcount,
    long pendingLeavesCount,
    long totalAssets,
    long assignedAssets,
    long inStockAssets,
    long underRepairAssets,
    long recentHiresCount,
    List<DepartmentDistribution> departmentDistribution,
    List<TimelineEventDTO> recentActivities
) {
    public record DepartmentDistribution(
        Long departmentId,
        String departmentName,
        String departmentCode,
        long employeeCount,
        double percentage
    ) {}
}
