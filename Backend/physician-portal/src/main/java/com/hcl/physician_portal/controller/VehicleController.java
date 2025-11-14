package com.hcl.physician_portal.controller;

import com.hcl.physician_portal.dto.ResponseDTO;
import com.hcl.physician_portal.dto.VehicleAssignmentResponse;
import com.hcl.physician_portal.dto.VehicleDTO;
import com.hcl.physician_portal.dto.ViewVehicle;
import com.hcl.physician_portal.model.Vehicle;
import com.hcl.physician_portal.model.SpecimenPickupRequest;
import com.hcl.physician_portal.mapper.VehicleMapper;
import com.hcl.physician_portal.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicle")
@CrossOrigin(origins = "*")
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;
    private final VehicleAssignmentWebSocketController vehicleAssignmentWebSocketController;

    public VehicleController(VehicleService vehicleService, VehicleMapper vehicleMapper, VehicleAssignmentWebSocketController vehicleAssignmentWebSocketController) {
        this.vehicleService = vehicleService;
        this.vehicleMapper = vehicleMapper;
        this.vehicleAssignmentWebSocketController = vehicleAssignmentWebSocketController;
    }

    @PostMapping("add-default-vehicles")
    public String addDefaultVehicles() {
        vehicleService.addDefaultVehicles();
        return "Added default vehicles";
    }

    @PostMapping("/assign")
    public ResponseEntity<ResponseDTO<?>> assignVehicle() {
        VehicleAssignmentResponse vehicleAssignmentResponse = vehicleService.assignVehicleToSpecimenRequest();
        if (vehicleAssignmentResponse == null) {
            return new ResponseEntity<>(
                    new ResponseDTO<Object>(null, "No vehicles are available currently. Please wait."),
                    HttpStatus.OK);
        }
        // Send WebSocket notification
        vehicleAssignmentWebSocketController.sendAssignmentNotification(vehicleAssignmentResponse);
        return ResponseEntity.ok(new ResponseDTO<>(vehicleAssignmentResponse, "Vehicle assigned successfully"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDTO<ViewVehicle>> updateVehicleStatus(@PathVariable("id") UUID id) {
        ViewVehicle viewVehicle = vehicleService.updateVehicleStatus(id);
        ResponseDTO<ViewVehicle> response = new ResponseDTO<>(viewVehicle, "Updated the vehicle status");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<?>> deleteVehicle(@PathVariable("id") UUID id) {
        boolean deleted = vehicleService.deleteVehicle(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addVehicle")
    public ResponseEntity<VehicleDTO> addVehicle(@RequestBody VehicleDTO vehicleDTO) {
        // Fetch specimen entities by IDs
        java.util.List<SpecimenPickupRequest> specimenRequests = new java.util.ArrayList<>();

        Vehicle vehicle = vehicleMapper.vehicleDTOtoEntity(vehicleDTO, specimenRequests);
        Vehicle savedVehicle = vehicleService.addVehicle(vehicle);
        VehicleDTO responseDTO = vehicleMapper.vehicleEntityToDTO(savedVehicle);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable("id") UUID id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        if (vehicle != null) {
            VehicleDTO dto = vehicleMapper.vehicleEntityToDTO(vehicle);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ViewVehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        List<ViewVehicle> dtos = new java.util.ArrayList<>();
        for (Vehicle v : vehicles) {
            dtos.add(vehicleMapper.vehicleEntityToView(v));
        }
        return ResponseEntity.ok(dtos);
    }
}
