package com.hcl.physician_portal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "workstations")
public class Workstation {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(nullable = false, unique = true)
        private String address;

        @Column(nullable = false)
        private String landmark;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        @ManyToMany(mappedBy = "workstations")
        private List<Physician> physicians;
}