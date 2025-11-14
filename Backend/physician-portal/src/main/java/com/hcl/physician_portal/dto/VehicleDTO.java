package com.hcl.physician_portal.dto;

import com.hcl.physician_portal.Enum.VehicleStatus;

public class VehicleDTO {
    private String vehicleNumber;
    private String currentAddress;
    private VehicleStatus status;

    // Getters and setters
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public String getCurrentAddress() { return currentAddress; }
    public void setCurrentAddress(String currentAddress) { this.currentAddress = currentAddress; }
    public VehicleStatus getStatus() { return status; }
    public void setStatus(VehicleStatus status) { this.status = status; }
}
