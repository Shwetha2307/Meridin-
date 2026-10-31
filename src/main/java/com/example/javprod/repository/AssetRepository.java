package com.example.javprod.repository;

import com.example.javprod.domain.Asset;
import com.example.javprod.domain.AssetCategory;
import com.example.javprod.domain.AssetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    boolean existsByAssetTag(String assetTag);

    boolean existsByAssetTagAndIdNot(String assetTag, Long id);

    boolean existsBySerialNumber(String serialNumber);

    boolean existsBySerialNumberAndIdNot(String serialNumber, Long id);

    long countByStatus(AssetStatus status);

    @Query("SELECT a FROM Asset a " +
           "LEFT JOIN FETCH a.assignedEmployee e " +
           "LEFT JOIN FETCH e.department d " +
           "WHERE (:status IS NULL OR a.status = :status) " +
           "AND (:category IS NULL OR a.category = :category) " +
           "ORDER BY a.assetTag ASC")
    List<Asset> searchAssets(@Param("status") AssetStatus status, @Param("category") AssetCategory category);

    @Query("SELECT a FROM Asset a " +
           "LEFT JOIN FETCH a.assignedEmployee e " +
           "LEFT JOIN FETCH e.department d " +
           "WHERE e.id = :employeeId")
    List<Asset> findByEmployeeId(@Param("employeeId") Long employeeId);
}
