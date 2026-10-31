package com.example.javprod.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "company_assets", indexes = {
    @Index(name = "idx_asset_tag", columnList = "assetTag", unique = true),
    @Index(name = "idx_asset_sn", columnList = "serialNumber", unique = true),
    @Index(name = "idx_asset_status", columnList = "status"),
    @Index(name = "idx_asset_employee", columnList = "employee_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "assignedEmployee")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "Asset tag is required")
    @Size(max = 30)
    @Column(nullable = false, unique = true, length = 30)
    private String assetTag;

    @NotBlank(message = "Asset name is required")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "Category is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AssetCategory category;

    @NotBlank(message = "Serial number is required")
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String serialNumber;

    private LocalDate assignedDate;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AssetStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee assignedEmployee;
}
