package com.example.javprod.service;

import com.example.javprod.domain.AssetCategory;
import com.example.javprod.domain.AssetStatus;
import com.example.javprod.dto.AssetRequest;
import com.example.javprod.dto.AssetResponse;

import java.util.List;

public interface AssetService {
    List<AssetResponse> getAssets(AssetStatus status, AssetCategory category);
    List<AssetResponse> getAssetsByEmployeeId(Long employeeId);
    AssetResponse getAssetById(Long id);
    AssetResponse createAsset(AssetRequest request);
    AssetResponse updateAsset(Long id, AssetRequest request);
    AssetResponse checkoutAsset(Long assetId, Long employeeId);
    AssetResponse returnAsset(Long assetId);
    AssetResponse sendToRepair(Long assetId);
    void deleteAsset(Long id);
}
