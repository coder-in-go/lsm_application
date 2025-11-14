package com.hcl.physician_portal.repository;

import com.hcl.physician_portal.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

// import java.util.List;
import java.util.UUID;

public interface IVehicleRepository extends JpaRepository<Vehicle, UUID> {
    // List<Vehicle> findAssignedVehicles();
}
