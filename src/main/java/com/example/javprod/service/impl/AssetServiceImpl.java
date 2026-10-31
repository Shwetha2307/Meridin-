package com.example.javprod.service.impl;

import com.example.javprod.domain.*;
import com.example.javprod.dto.AssetRequest;
import com.example.javprod.dto.AssetResponse;
import com.example.javprod.exception.DuplicateResourceException;
import com.example.javprod.exception.InvalidOperationException;
import com.example.javprod.exception.ResourceNotFoundException;
import com.example.javprod.repository.AssetRepository;
import com.example.javprod.repository.EmployeeRepository;
import com.example.javprod.service.AssetService;
import com.example.javprod.service.EmployeeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeHistoryService historyService;

    @Override
    public List<AssetResponse> getAssets(AssetStatus status, AssetCategory category) {
        return assetRepository.searchAssets(status, category).stream()
            .map(AssetResponse::fromEntity)
            .toList();
    }

    @Override
    public List<AssetResponse> getAssetsByEmployeeId(Long employeeId) {
        return assetRepository.findByEmployeeId(employeeId).stream()
            .map(AssetResponse::fromEntity)
            .toList();
    }

    @Override
    public AssetResponse getAssetById(Long id) {
        Asset asset = assetRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with ID: " + id));
        return AssetResponse.fromEntity(asset);
    }

    @Override
    @Transactional
    public AssetResponse createAsset(AssetRequest req) {
        String tag = req.assetTag().trim().toUpperCase();
        String sn = req.serialNumber().trim();

        if (assetRepository.existsByAssetTag(tag)) {
            throw new DuplicateResourceException("Asset with tag '" + tag + "' already exists.");
        }
        if (assetRepository.existsBySerialNumber(sn)) {
            throw new DuplicateResourceException("Asset with serial number '" + sn + "' already exists.");
        }

        Employee assignedEmp = null;
        LocalDate assignedDate = null;
        AssetStatus finalStatus = req.status();

        if (req.assignedEmployeeId() != null) {
            assignedEmp = employeeRepository.findById(req.assignedEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + req.assignedEmployeeId()));
            finalStatus = AssetStatus.ASSIGNED;
            assignedDate = LocalDate.now();
        }

        Asset asset = Asset.builder()
            .assetTag(tag)
            .name(req.name().trim())
            .category(req.category())
            .serialNumber(sn)
            .assignedDate(assignedDate)
            .status(finalStatus)
            .assignedEmployee(assignedEmp)
            .build();

        Asset saved = assetRepository.save(asset);

        if (assignedEmp != null) {
            historyService.recordEvent(
                assignedEmp,
                AuditEventType.STATUS_CHANGE,
                String.format("Assigned corporate asset: %s (%s, SN: %s)", saved.getName(), saved.getAssetTag(), saved.getSerialNumber())
            );
        }

        return AssetResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public AssetResponse updateAsset(Long id, AssetRequest req) {
        Asset asset = assetRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with ID: " + id));

        String tag = req.assetTag().trim().toUpperCase();
        String sn = req.serialNumber().trim();

        if (assetRepository.existsByAssetTagAndIdNot(tag, id)) {
            throw new DuplicateResourceException("Asset with tag '" + tag + "' already exists.");
        }
        if (assetRepository.existsBySerialNumberAndIdNot(sn, id)) {
            throw new DuplicateResourceException("Asset with serial number '" + sn + "' already exists.");
        }

        asset.setAssetTag(tag);
        asset.setName(req.name().trim());
        asset.setCategory(req.category());
        asset.setSerialNumber(sn);

        if (req.assignedEmployeeId() != null) {
            Employee assignedEmp = employeeRepository.findById(req.assignedEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + req.assignedEmployeeId()));
            asset.setAssignedEmployee(assignedEmp);
            asset.setStatus(AssetStatus.ASSIGNED);
            if (asset.getAssignedDate() == null) {
                asset.setAssignedDate(LocalDate.now());
            }
        } else {
            asset.setAssignedEmployee(null);
            asset.setAssignedDate(null);
            asset.setStatus(req.status() != null ? req.status() : AssetStatus.IN_STOCK);
        }

        Asset updated = assetRepository.save(asset);
        return AssetResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public AssetResponse checkoutAsset(Long assetId, Long employeeId) {
        Asset asset = assetRepository.findById(assetId)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with ID: " + assetId));

        Employee emp = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        if (emp.getStatus() == EmployeeStatus.TERMINATED) {
            throw new InvalidOperationException("Cannot checkout asset to a terminated employee.");
        }

        asset.setAssignedEmployee(emp);
        asset.setAssignedDate(LocalDate.now());
        asset.setStatus(AssetStatus.ASSIGNED);

        Asset updated = assetRepository.save(asset);

        historyService.recordEvent(
            emp,
            AuditEventType.STATUS_CHANGE,
            String.format("Checked out hardware: %s (%s)", asset.getName(), asset.getAssetTag())
        );

        return AssetResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public AssetResponse returnAsset(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with ID: " + assetId));

        Employee previousHolder = asset.getAssignedEmployee();

        asset.setAssignedEmployee(null);
        asset.setAssignedDate(null);
        asset.setStatus(AssetStatus.IN_STOCK);

        Asset updated = assetRepository.save(asset);

        if (previousHolder != null) {
            historyService.recordEvent(
                previousHolder,
                AuditEventType.STATUS_CHANGE,
                String.format("Returned corporate hardware: %s (%s)", asset.getName(), asset.getAssetTag())
            );
        }

        return AssetResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public AssetResponse sendToRepair(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with ID: " + assetId));

        asset.setAssignedEmployee(null);
        asset.setAssignedDate(null);
        asset.setStatus(AssetStatus.UNDER_REPAIR);

        Asset updated = assetRepository.save(asset);
        return AssetResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with ID: " + id));

        if (asset.getStatus() == AssetStatus.ASSIGNED) {
            throw new InvalidOperationException("Cannot delete asset while currently assigned to an employee. Return it first.");
        }

        assetRepository.delete(asset);
    }
}
