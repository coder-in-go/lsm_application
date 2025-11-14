
package com.hcl.physician_portal.controller;

import com.hcl.physician_portal.dto.ResponseDTO;
import com.hcl.physician_portal.dto.SpecimenPickupRequestDTO;
import com.hcl.physician_portal.dto.ViewSpecimenPickupRequest;
import com.hcl.physician_portal.service.SpecimenPickupRequestService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/specimen-pickup-request")
public class SpecimenPickupRequestController {
    @GetMapping("/physician-id-by-mobile")
    public ResponseEntity<Map<String, Object>> getPhysicianIdByMobile(@RequestParam String mobileNumber) {
        Map<String, Object> result = new HashMap<>();
        var physicianOpt = specimenPickupRequestService.getPhysicianByMobileNumber(mobileNumber);
        if (physicianOpt.isPresent()) {
            result.put("physicianId", physicianOpt.get().getId());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("error", "Physician not found");
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @Autowired
    private SpecimenPickupRequestService specimenPickupRequestService;

    @GetMapping("/request-stats")
    public ResponseEntity<Map<String, Object>> getPhysicianRequestStats() {
        Map<String, Object> stats = specimenPickupRequestService.getPhysicianDailyRequestStats();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @GetMapping("/request-status-summary")
    public ResponseEntity<Map<String, Object>> getPhysicianRequestStatusSummary() {
        Map<String, Object> summary = specimenPickupRequestService.getPhysicianRequestStatusSummary();
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }

    @PostMapping("/add-default-specimenpickup-requests")
    public ResponseEntity<ResponseDTO<List<ViewSpecimenPickupRequest>>> addDefaultSpecimenPickupRequests() {
        // List<ViewSpecimenPickupRequest> savedRequests =
        ResponseDTO<List<ViewSpecimenPickupRequest>> response = new ResponseDTO<>(
                specimenPickupRequestService.addDefaultSpecimenPickupRequests(),
                "Added the default specimen pickup requests to the DB successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/specimen-pickup-request")
    public ResponseEntity<ResponseDTO<ViewSpecimenPickupRequest>> createPickupRequest(
            @RequestBody SpecimenPickupRequestDTO specimenPickupRequestDTO) {
        ResponseDTO<ViewSpecimenPickupRequest> response = new ResponseDTO<>(
                specimenPickupRequestService.createSpecimenPickupRequest(specimenPickupRequestDTO),
                "Created new Specimen Pickup Request successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("specimen-pickup-request/{id}")
    public ResponseEntity<ResponseDTO<ViewSpecimenPickupRequest>> getSpecimenRequestById(@PathVariable UUID id) {
        ResponseDTO<ViewSpecimenPickupRequest> response = new ResponseDTO<>(
                specimenPickupRequestService.getSpecimenRequestById(id),
                "Fetched the request with ID - " + id + " successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/specimen-pickup-requests")
    public ResponseEntity<ResponseDTO<List<ViewSpecimenPickupRequest>>> getAllSpecimenRequests() {
        ResponseDTO<List<ViewSpecimenPickupRequest>> response = new ResponseDTO<>(
                specimenPickupRequestService.getAllSpecimenRequests(),
                "Fetched all specimen pickup requests successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("specimen-pickup-request/{id}/status")
    public ResponseEntity<ResponseDTO<ViewSpecimenPickupRequest>> updateSpecimenRequestStatus(@PathVariable UUID id,
            @RequestParam String status) {
        ResponseDTO<ViewSpecimenPickupRequest> response = new ResponseDTO<>(
                specimenPickupRequestService.updateSpecimenRequestStatus(id, status),
                "Updated the status of the request with ID - " + id + " successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}