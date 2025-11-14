package com.hcl.physician_portal.repository;

import com.hcl.physician_portal.model.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPhysicianRepository extends JpaRepository<Physician, UUID> {
    Optional<Physician> findByMobileNumber(String mobileNumber);
}