package com.hcl.physician_portal.model;

import com.hcl.physician_portal.Enum.PickupStatus;
import com.hcl.physician_portal.Enum.PickupType;
import com.hcl.physician_portal.Enum.TemperatureInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "specimen_pickup_requests")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SpecimenPickupRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime scheduledDate;

    @Column(nullable = false)
    private String pickupAddress;

    @Enumerated(EnumType.STRING)
    private TemperatureInfo temperatureInfo;

    @Enumerated(EnumType.STRING)
    private PickupType pickupType;

    private LocalDateTime closureTime;

    private LocalDateTime pickupRequestTimeBefore;
    private LocalDateTime pickupRequestTimeAfter;

    @Enumerated(EnumType.STRING)
    private PickupStatus status;

    @Column(name = "estimated_time_of_arrival")
    private String estimatedTimeOfArrival;

    @Column(name = "route_id")
    private String routeID;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle assignedVehicle;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "physician_id", nullable = false)
    private Physician physician;

}