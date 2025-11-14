package com.hcl.physician_portal.mapper;

import com.hcl.physician_portal.dto.GeoCodeDTO;
// import com.hcl.physician_portal.dto.GeoCodeDTO;
import com.hcl.physician_portal.dto.SpecimenPickupRequestDTO;
import com.hcl.physician_portal.dto.ViewSpecimenPickupRequest;
import com.hcl.physician_portal.helpers.GeoCodeHelper;
import com.hcl.physician_portal.model.SpecimenPickupRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpecimenPickupRequestMapper {
    private final GeoCodeHelper geoCodeHelper;

    public SpecimenPickupRequestMapper(GeoCodeHelper geoCodeHelper) {
        this.geoCodeHelper = geoCodeHelper;
    }

    public SpecimenPickupRequest specimenPickupRequestDTOToEntity(SpecimenPickupRequestDTO specimenPickupRequestDTO) {
        SpecimenPickupRequest specimenPickupRequest = new SpecimenPickupRequest();

        specimenPickupRequest.setScheduledDate(specimenPickupRequestDTO.getScheduledDate());
        specimenPickupRequest.setClosureTime(specimenPickupRequestDTO.getClosureTime());
        specimenPickupRequest.setPickupAddress(specimenPickupRequestDTO.getPickupAddress());
        specimenPickupRequest.setStatus(specimenPickupRequestDTO.getStatus());
        specimenPickupRequest.setTemperatureInfo(specimenPickupRequestDTO.getTemperatureInfo());
        specimenPickupRequest.setPickupRequestTimeBefore(specimenPickupRequestDTO.getPickupRequestTimeBefore());
        specimenPickupRequest.setPickupRequestTimeAfter(specimenPickupRequestDTO.getPickupRequestTimeAfter());
        specimenPickupRequest.setPickupType(specimenPickupRequestDTO.getPickupType());
        specimenPickupRequest.setCreatedAt(LocalDateTime.now());
        specimenPickupRequest.setUpdatedAt(LocalDateTime.now());
        // specimenPickupRequest.setRouteID(geoCodeHelper.generateRouteId(specimenPickupRequestDTO.getPickupAddress(),
        // "Anna Nagar, Madurai, Madurai"));

        // 🌍 Geocode the pickup address
        GeoCodeDTO geo = geoCodeHelper.geocodeLatitudeLongitude(specimenPickupRequestDTO.getPickupAddress());

        // 🧠 Assign route ID based on grid
        String routeId = geoCodeHelper.generateRouteIdFromGrid(geo.getLatitude(),
                geo.getLongitude());
        specimenPickupRequest.setRouteID(routeId);

        System.out.println("RouteID------------------------>" + specimenPickupRequest.getRouteID());

        // specimenPickupRequestDTO.setAssignedVehicleId(specimenPickupRequest.getAssignedVehicleId());

        return specimenPickupRequest;
    }

    public ViewSpecimenPickupRequest specimenPickupRequestEntityToView(SpecimenPickupRequest specimenPickupRequest) {
        ViewSpecimenPickupRequest viewSpecimenPickupRequest = new ViewSpecimenPickupRequest();

        viewSpecimenPickupRequest.setId(specimenPickupRequest.getId());
        viewSpecimenPickupRequest.setScheduledDate(specimenPickupRequest.getScheduledDate());
        viewSpecimenPickupRequest.setPickupAddress(specimenPickupRequest.getPickupAddress());
        viewSpecimenPickupRequest.setStatus(specimenPickupRequest.getStatus());
        viewSpecimenPickupRequest.setTemperatureInfo(specimenPickupRequest.getTemperatureInfo());
        viewSpecimenPickupRequest.setPickupType(specimenPickupRequest.getPickupType());
        viewSpecimenPickupRequest.setClosureTime(specimenPickupRequest.getClosureTime());
        viewSpecimenPickupRequest.setPickupRequestTimeBefore(specimenPickupRequest.getPickupRequestTimeBefore());
        viewSpecimenPickupRequest.setPickupRequestTimeAfter(specimenPickupRequest.getPickupRequestTimeAfter());
        specimenPickupRequest.setAssignedVehicle(specimenPickupRequest.getAssignedVehicle());
        viewSpecimenPickupRequest.setRouteID(specimenPickupRequest.getRouteID());

        viewSpecimenPickupRequest.setCreatedAt(specimenPickupRequest.getCreatedAt());
        viewSpecimenPickupRequest.setUpdatedAt(specimenPickupRequest.getUpdatedAt());

        // if (specimenPickupRequest.getEstimatedTimeOfArrival() != null) {
        viewSpecimenPickupRequest.setEstimatedTimeOfArrival(specimenPickupRequest.getEstimatedTimeOfArrival());
        // }

        return viewSpecimenPickupRequest;

    }

    public List<ViewSpecimenPickupRequest> specimenPickupRequestListToViewList(List<SpecimenPickupRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return List.of();
        }
        return requests.stream()
                .map(this::specimenPickupRequestEntityToView)
                .collect(Collectors.toList());
    }

}
