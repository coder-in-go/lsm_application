package com.hcl.physician_portal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "physicians")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Physician {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false)
    private int yearsOfExperience;

    @Column(name = "mobile_number", nullable = false, unique = true)
    private String mobileNumber;

    @Column(name = "email_id", nullable = false, unique = true)
    private String emailID;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(name = "physician_workstation", joinColumns = @JoinColumn(name = "physician_id"), inverseJoinColumns = @JoinColumn(name = "workstation_id"))
    private List<Workstation> workstations;

    @OneToMany(mappedBy = "physician", cascade = CascadeType.ALL)
    private List<SpecimenPickupRequest> pickupRequests;
}