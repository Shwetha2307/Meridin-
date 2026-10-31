package com.example.javprod.service.impl;

import com.example.javprod.domain.AssetStatus;
import com.example.javprod.domain.Department;
import com.example.javprod.domain.EmployeeStatus;
import com.example.javprod.domain.LeaveStatus;
import com.example.javprod.dto.AnalyticsSummaryDTO;
import com.example.javprod.dto.TimelineEventDTO;
import com.example.javprod.repository.*;
import com.example.javprod.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final AssetRepository assetRepository;
    private final EmployeeHistoryRepository historyRepository;

    @Override
    public AnalyticsSummaryDTO getSummary() {
        long totalHeadcount = employeeRepository.count();
        long activeHeadcount = employeeRepository.countByStatus(EmployeeStatus.ACTIVE);
        long onLeaveHeadcount = employeeRepository.countByStatus(EmployeeStatus.ON_LEAVE);
        long terminatedHeadcount = employeeRepository.countByStatus(EmployeeStatus.TERMINATED);

        long pendingLeaves = leaveRequestRepository.countByStatus(LeaveStatus.PENDING);

        long totalAssets = assetRepository.count();
        long assignedAssets = assetRepository.countByStatus(AssetStatus.ASSIGNED);
        long inStockAssets = assetRepository.countByStatus(AssetStatus.IN_STOCK);
        long underRepairAssets = assetRepository.countByStatus(AssetStatus.UNDER_REPAIR);

        LocalDate ninetyDaysAgo = LocalDate.now().minusDays(90);
        long recentHires = employeeRepository.countByHireDateAfter(ninetyDaysAgo);

        // Department distribution
        List<Department> departments = departmentRepository.findAll();
        List<AnalyticsSummaryDTO.DepartmentDistribution> distributions = new ArrayList<>();

        for (Department dept : departments) {
            long count = employeeRepository.countByDepartmentId(dept.getId());
            double pct = totalHeadcount > 0 ? ((double) count / totalHeadcount) * 100.0 : 0.0;
            // Round to 1 decimal place
            double roundedPct = Math.round(pct * 10.0) / 10.0;
            distributions.add(new AnalyticsSummaryDTO.DepartmentDistribution(
                dept.getId(),
                dept.getName(),
                dept.getCode(),
                count,
                roundedPct
            ));
        }

        // Recent activity feed
        List<TimelineEventDTO> activities = historyRepository.findRecentActivities().stream()
            .map(TimelineEventDTO::fromEntity)
            .toList();

        return new AnalyticsSummaryDTO(
            totalHeadcount,
            activeHeadcount,
            onLeaveHeadcount,
            terminatedHeadcount,
            pendingLeaves,
            totalAssets,
            assignedAssets,
            inStockAssets,
            underRepairAssets,
            recentHires,
            distributions,
            activities
        );
    }

    @Override
    public List<TimelineEventDTO> getRecentTimeline() {
        return historyRepository.findRecentActivities().stream()
            .map(TimelineEventDTO::fromEntity)
            .toList();
    }
}
