package com.hcl.physician_portal.mapper;

import com.hcl.physician_portal.dto.VehicleAssignmentResponse;
import com.hcl.physician_portal.dto.VehicleDTO;
import com.hcl.physician_portal.dto.ViewAssignedSpecimenRequest;
import com.hcl.physician_portal.dto.ViewVehicle;
import com.hcl.physician_portal.model.Vehicle;
import com.hcl.physician_portal.model.SpecimenPickupRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class VehicleMapper {
    public Vehicle vehicleDTOtoEntity(VehicleDTO dto, List<SpecimenPickupRequest> specimenRequests) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(dto.getVehicleNumber());
        vehicle.setCurrentAddress(dto.getCurrentAddress());
        // vehicle.setLatitude(dto.getLatitude());
        // vehicle.setLongitude(dto.getLongitude());
        vehicle.setStatus(dto.getStatus());
        vehicle.setSpecimenPickupRequests(specimenRequests);
        return vehicle;
    }

    public VehicleDTO vehicleEntityToDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setVehicleNumber(vehicle.getVehicleNumber());
        dto.setCurrentAddress(vehicle.getCurrentAddress());
        dto.setStatus(vehicle.getStatus());
        List<UUID> specimenIds = new ArrayList<>();
        if (vehicle.getSpecimenPickupRequests() != null) {
            for (SpecimenPickupRequest req : vehicle.getSpecimenPickupRequests()) {
                specimenIds.add(req.getId());
            }
        }
        // dto.setSpecimenRequestIds(specimenIds);
        return dto;
    }

    public VehicleAssignmentResponse mapToVehicleAssignmentResponse(Vehicle availableVehicle,
            List<SpecimenPickupRequest> specimenpickupRequestList) {
        VehicleAssignmentResponse vehicleAssignmentResponse = new VehicleAssignmentResponse();

        vehicleAssignmentResponse.setVehicleNumber(availableVehicle.getVehicleNumber());
        vehicleAssignmentResponse.setVehicleStatus(availableVehicle.getStatus());
        vehicleAssignmentResponse.setCurrentAddress(availableVehicle.getCurrentAddress());

        // vehicleAssignmentResponse.setMessage("Assigned vehicle " +
        // availableVehicle.getVehicleNumber() +
        // " to specimen request " + specimenpickupRequest.getId());

        List<ViewAssignedSpecimenRequest> viewAssignedSpecimenRequests = specimenpickupRequestList.stream().map(req -> {
            ViewAssignedSpecimenRequest viewAssignedSpecimenRequest = new ViewAssignedSpecimenRequest();
            viewAssignedSpecimenRequest.setId(req.getId());
            viewAssignedSpecimenRequest.setPickupAddress(req.getPickupAddress());
            viewAssignedSpecimenRequest.setScheduledDate(req.getScheduledDate());
            viewAssignedSpecimenRequest.setPickupType(req.getPickupType());
            viewAssignedSpecimenRequest.setTemperatureInfo(req.getTemperatureInfo());
            viewAssignedSpecimenRequest.setClosureTime(req.getClosureTime());
            viewAssignedSpecimenRequest.setRouteID(req.getRouteID());
            viewAssignedSpecimenRequest.setEstimatedTimeOfArrival(req.getEstimatedTimeOfArrival());
            viewAssignedSpecimenRequest.setStatus(req.getStatus());
            return viewAssignedSpecimenRequest;
        }).collect(Collectors.toList());

        vehicleAssignmentResponse.setAssignedSpecimenRequestList(viewAssignedSpecimenRequests);

        return vehicleAssignmentResponse;
    }

    public ViewVehicle vehicleEntityToView(Vehicle vehicle) {
        ViewVehicle viewVehicle = new ViewVehicle();

        viewVehicle.setId(vehicle.getVehicleId());
        viewVehicle.setVehicleNumber(vehicle.getVehicleNumber());
        viewVehicle.setCurrentAddress(vehicle.getCurrentAddress());
        viewVehicle.setStatus(vehicle.getStatus().name()); // assuming status is an Enum
        // viewVehicle.setEstimatedTimeOfArrival(vehicle.getEstimatedTimeOfArrival());

        return viewVehicle;
    }
}
