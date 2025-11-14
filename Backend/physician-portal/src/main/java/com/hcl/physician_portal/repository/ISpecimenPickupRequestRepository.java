package com.hcl.physician_portal.repository;

import com.hcl.physician_portal.model.SpecimenPickupRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ISpecimenPickupRequestRepository extends JpaRepository<SpecimenPickupRequest, UUID> {
    @Query("SELECT s FROM SpecimenPickupRequest s WHERE s.physician.id = :physicianId")
    List<SpecimenPickupRequest> findAllRequestsByPhysician(@Param("physicianId") UUID physicianId);

}