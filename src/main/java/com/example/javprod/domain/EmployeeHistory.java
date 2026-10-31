package com.example.javprod.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_histories", indexes = {
    @Index(name = "idx_history_emp", columnList = "employee_id"),
    @Index(name = "idx_history_time", columnList = "timestamp")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "employee")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EmployeeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull(message = "Employee is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @NotNull(message = "Event type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AuditEventType eventType;

    @NotBlank(message = "Description is required")
    @Column(nullable = false, length = 1000)
    private String description;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    public void onPrePersist() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
