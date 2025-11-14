package com.hcl.physician_portal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.physician_portal.Enum.PickupStatus;
import com.hcl.physician_portal.Enum.PickupType;
import com.hcl.physician_portal.Enum.TemperatureInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpecimenPickupRequestDTO {
    @JsonProperty("scheduled_date")
    private LocalDateTime scheduledDate;

    @JsonProperty("pickup_address")
    private String pickupAddress;

    @JsonProperty("temperature_info")
    private TemperatureInfo temperatureInfo;

    @JsonProperty("pickup_type")
    private PickupType pickupType;

    @JsonProperty("closure_time")
    private LocalDateTime closureTime;

    @JsonProperty("pickup_request_time_before")
    private LocalDateTime pickupRequestTimeBefore;

    @JsonProperty("pickup_request_time_after")
    private LocalDateTime pickupRequestTimeAfter;

    @JsonProperty("status")
    private PickupStatus status;

}