package com.hcl.physician_portal.controller;

import com.hcl.physician_portal.dto.ResponseDTO;
import com.hcl.physician_portal.dto.ViewWorkstation;
import com.hcl.physician_portal.service.WorkstationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/workstation")
public class WorkstationController {

    private final WorkstationService workstationService;

    public WorkstationController(WorkstationService workstationService) {
        this.workstationService = workstationService;
    }

    @PostMapping("/add-default-workstations")
    public ResponseEntity<ResponseDTO<List<ViewWorkstation>>> addWorkstations() {
        ResponseDTO<List<ViewWorkstation>> response = new ResponseDTO<>(workstationService.addWorkstations(),
                "Added default workstations successfully");
        return new ResponseEntity<ResponseDTO<List<ViewWorkstation>>>(response, HttpStatus.CREATED);
    }

    @GetMapping("/workstations")
    public ResponseEntity<ResponseDTO<List<ViewWorkstation>>> getAllWorkStations() {
        ResponseDTO<List<ViewWorkstation>> response = new ResponseDTO<>(workstationService.getAllUserWorkstations(),
                "Fetched successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}