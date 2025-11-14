package com.hcl.physician_portal.dto;

import java.util.List;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.physician_portal.Enum.VehicleStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VehicleAssignmentResponse {

    @JsonProperty("vehicle_no")
    private String vehicleNumber;

    @JsonProperty("vehicle_status")
    private VehicleStatus vehicleStatus;

    @JsonProperty("current_address")
    private String currentAddress;

    private List<ViewAssignedSpecimenRequest> assignedSpecimenRequestList;

    // @JsonProperty("message")
    // private String message;
}
