package com.hcl.physician_portal.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ViewVehicle {
    @JsonProperty("id")
    private UUID id;

    // @JsonProperty("estimated_time_of_arrival")
    // private String estimatedTimeOfArrival;

    @JsonProperty("vehicle_number")
    private String vehicleNumber;

    @JsonProperty("current_address")
    private String currentAddress;

    @JsonProperty("status")
    private String status;

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // public String getEstimatedTimeOfArrival() {
    // return estimatedTimeOfArrival;
    // }

    // public void setEstimatedTimeOfArrival(String estimatedTimeOfArrival) {
    // this.estimatedTimeOfArrival = estimatedTimeOfArrival;
    // }
}
