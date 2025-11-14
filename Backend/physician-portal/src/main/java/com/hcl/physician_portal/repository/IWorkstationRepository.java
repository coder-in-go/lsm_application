package com.hcl.physician_portal.repository;

import com.hcl.physician_portal.model.Workstation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IWorkstationRepository extends JpaRepository<Workstation, UUID> {

    @Query("SELECT w FROM Workstation w JOIN w.physicians p WHERE p.id = :physicianID")
    List<Workstation> findByPhysicianId(@Param("physicianID") UUID physicianID);

    List<Workstation> findByAddressIn(List<String> addresses);
}