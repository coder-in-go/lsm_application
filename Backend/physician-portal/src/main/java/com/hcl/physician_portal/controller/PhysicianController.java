package com.hcl.physician_portal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.physician_portal.service.PhysicianService;

@RestController
@RequestMapping("/api/v1/physician")
@CrossOrigin(origins = "*")
public class PhysicianController {

    private final PhysicianService physicianService;

    public PhysicianController(PhysicianService physicianService) {
        this.physicianService = physicianService;
    }

    @PostMapping("/add-default-physicians")
    public ResponseEntity<String> addDefaultPhysicians() {
        String message = physicianService.addDefaultPhysicians();
        return ResponseEntity.ok(message);
    }

}