package com.hcl.physician_portal.model;

import com.hcl.physician_portal.Enum.VehicleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID vehicleId;

    @Column(nullable = false, unique = true)
    private String vehicleNumber;

    private String currentAddress;
    // private Double latitude;
    // private Double longitude;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "assignedVehicle")
    private List<SpecimenPickupRequest> specimenPickupRequests;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
