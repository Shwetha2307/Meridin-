package com.example.javprod.web;

import com.example.javprod.domain.AssetCategory;
import com.example.javprod.domain.AssetStatus;
import com.example.javprod.dto.AssetRequest;
import com.example.javprod.dto.AssetResponse;
import com.example.javprod.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @GetMapping
    public ResponseEntity<List<AssetResponse>> getAssets(
        @RequestParam(name = "status", required = false) AssetStatus status,
        @RequestParam(name = "category", required = false) AssetCategory category
    ) {
        return ResponseEntity.ok(assetService.getAssets(status, category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponse> getAssetById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AssetResponse>> getAssetsByEmployeeId(@PathVariable("employeeId") Long employeeId) {
        return ResponseEntity.ok(assetService.getAssetsByEmployeeId(employeeId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ASSET_WRITE') or hasAnyRole('ADMIN', 'HR_MANAGER')")
    public ResponseEntity<AssetResponse> createAsset(@Valid @RequestBody AssetRequest request) {
        AssetResponse created = assetService.createAsset(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ASSET_WRITE') or hasAnyRole('ADMIN', 'HR_MANAGER')")
    public ResponseEntity<AssetResponse> updateAsset(@PathVariable("id") Long id, @Valid @RequestBody AssetRequest request) {
        AssetResponse updated = assetService.updateAsset(id, request);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/checkout")
    @PreAuthorize("hasAuthority('ASSET_WRITE') or hasAnyRole('ADMIN', 'HR_MANAGER')")
    public ResponseEntity<AssetResponse> checkoutAsset(
        @PathVariable("id") Long id,
        @RequestBody Map<String, Long> payload
    ) {
        Long employeeId = payload.get("employeeId");
        if (employeeId == null) {
            return ResponseEntity.badRequest().build();
        }
        AssetResponse updated = assetService.checkoutAsset(id, employeeId);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasAuthority('ASSET_WRITE') or hasAnyRole('ADMIN', 'HR_MANAGER')")
    public ResponseEntity<AssetResponse> returnAsset(@PathVariable("id") Long id) {
        AssetResponse updated = assetService.returnAsset(id);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/repair")
    @PreAuthorize("hasAuthority('ASSET_WRITE') or hasAnyRole('ADMIN', 'HR_MANAGER')")
    public ResponseEntity<AssetResponse> sendToRepair(@PathVariable("id") Long id) {
        AssetResponse updated = assetService.sendToRepair(id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ORG_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAsset(@PathVariable("id") Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
}
