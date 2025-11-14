package com.hcl.physician_portal.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hcl.physician_portal.dto.PhysicianDTO;
import com.hcl.physician_portal.model.Physician;
import com.hcl.physician_portal.model.Workstation;
import com.hcl.physician_portal.repository.IPhysicianRepository;
import com.hcl.physician_portal.repository.IWorkstationRepository;

@Service
public class PhysicianService {

    private final IPhysicianRepository iPhysicianRepository;
    private final IWorkstationRepository iWorkstationRepository;

    public PhysicianService(IPhysicianRepository ipPhysicianRepository, IWorkstationRepository iWorkstationRepository) {
        this.iPhysicianRepository = ipPhysicianRepository;
        this.iWorkstationRepository = iWorkstationRepository;
    }

    public Physician addPhysician(PhysicianDTO request) {
        Physician physician = new Physician();
        physician.setName(request.getName());
        physician.setDob(request.getDob());
        physician.setYearsOfExperience(request.getYearsOfExperience());
        physician.setMobileNumber(request.getMobileNumber());
        physician.setEmailID(request.getEmailID());
        physician.setCreatedAt(LocalDateTime.now());
        physician.setUpdatedAt(LocalDateTime.now());

        // Fetch workstations by address
        List<Workstation> workstations = iWorkstationRepository.findByAddressIn(request.getWorkstationAddresses());
        physician.setWorkstations(workstations);

        // Also update the reverse mapping
        for (Workstation ws : workstations) {
            if (ws.getPhysicians() == null) {
                ws.setPhysicians(new ArrayList<>());
            }
            ws.getPhysicians().add(physician);
        }

        return iPhysicianRepository.save(physician);
    }

    public String addDefaultPhysicians() {
        List<String> workstationAddresses = List.of(
                "Rio Hospital Madurai",
                "Vikram Hospital Madurai",
                "Madurai Junction");

        List<Workstation> workstations = iWorkstationRepository.findByAddressIn(workstationAddresses);

        Physician physician1 = new Physician();
        physician1.setName("Dr. Haritha");
        physician1.setDob(LocalDate.of(2001, 12, 26));
        physician1.setYearsOfExperience(2);
        physician1.setMobileNumber("9876543210");
        physician1.setEmailID("haritha.s@gmail.com");
        physician1.setCreatedAt(LocalDateTime.now());
        physician1.setUpdatedAt(LocalDateTime.now());
        physician1.setWorkstations(workstations);

        Physician physician2 = new Physician();
        physician2.setName("Dr. Sudharsan");
        physician2.setDob(LocalDate.of(2001, 8, 22));
        physician2.setYearsOfExperience(2);
        physician2.setMobileNumber("9123456780");
        physician2.setEmailID("sudharsan@gmail.com");
        physician2.setCreatedAt(LocalDateTime.now());
        physician2.setUpdatedAt(LocalDateTime.now());
        physician2.setWorkstations(List.of(workstations.get(0), workstations.get(2))); // partial mapping

        // Update reverse mapping
        for (Workstation ws : workstations) {
            if (ws.getPhysicians() == null) {
                ws.setPhysicians(new ArrayList<>());
            }
            ws.getPhysicians().add(physician1);
        }

        workstations.get(0).getPhysicians().add(physician2);
        workstations.get(2).getPhysicians().add(physician2);

        iPhysicianRepository.saveAll(List.of(physician1, physician2));

        return "Default physicians added and mapped to workstations.";
    }

}