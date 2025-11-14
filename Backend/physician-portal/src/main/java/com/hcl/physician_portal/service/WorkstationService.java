package com.hcl.physician_portal.service;

import com.hcl.physician_portal.dto.ViewWorkstation;
import com.hcl.physician_portal.exception.PhysicianNotFoundException;
import com.hcl.physician_portal.mapper.WorkstationMapper;
import com.hcl.physician_portal.model.Physician;
import com.hcl.physician_portal.model.Workstation;
import com.hcl.physician_portal.repository.IPhysicianRepository;
import com.hcl.physician_portal.repository.IWorkstationRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkstationService {

    private final IPhysicianRepository iPhysicianRepository;
    private final WorkstationMapper workstationMapper;
    private final IWorkstationRepository iWorkstationRepository;

    public WorkstationService(IPhysicianRepository iPhysicianRepository, WorkstationMapper workstationMapper,
            IWorkstationRepository workstationRepository) {
        this.iPhysicianRepository = iPhysicianRepository;
        this.workstationMapper = workstationMapper;
        this.iWorkstationRepository = workstationRepository;
    }

    public List<ViewWorkstation> getAllUserWorkstations() {

        Physician physician = iPhysicianRepository.findByMobileNumber("9876543210")
                .orElseThrow(() -> new PhysicianNotFoundException("Physician not found"));

        List<Workstation> workstationList = physician.getWorkstations();

        return workstationMapper.workstationEntityListToViewList(workstationList);
    }

    public List<ViewWorkstation> addWorkstations() {
        List<Workstation> workstations = new ArrayList<>();

        // Create default workstations
        Workstation ws1 = new Workstation();
        ws1.setAddress("Rio Hospital Madurai");
        ws1.setLandmark("Madurai");
        ws1.setCreatedAt(LocalDateTime.now());
        ws1.setUpdatedAt(LocalDateTime.now());

        Workstation ws2 = new Workstation();
        ws2.setAddress("Vikram Multispeciality Hospital Madurai");
        ws2.setLandmark("Madurai");
        ws2.setCreatedAt(LocalDateTime.now());
        ws2.setUpdatedAt(LocalDateTime.now());

        Workstation ws3 = new Workstation();
        ws3.setAddress("Madurai Junction");
        ws3.setLandmark("Madurai");
        ws3.setCreatedAt(LocalDateTime.now());
        ws3.setUpdatedAt(LocalDateTime.now());

        // Add to list
        workstations.add(ws1);
        workstations.add(ws2);
        workstations.add(ws3);

        // Save all
        iWorkstationRepository.saveAll(workstations);

        return workstationMapper.workstationEntityListToViewList(workstations);
    }
}