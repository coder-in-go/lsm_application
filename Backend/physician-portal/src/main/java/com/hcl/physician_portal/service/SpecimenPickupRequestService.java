package com.hcl.physician_portal.service;

import com.hcl.physician_portal.Enum.TemperatureInfo;
import com.hcl.physician_portal.dto.SpecimenPickupRequestDTO;
import com.hcl.physician_portal.dto.ViewSpecimenPickupRequest;
import com.hcl.physician_portal.exception.PhysicianNotFoundException;
import com.hcl.physician_portal.mapper.SpecimenPickupRequestMapper;
import com.hcl.physician_portal.model.Physician;
import com.hcl.physician_portal.model.SpecimenPickupRequest;
import com.hcl.physician_portal.repository.IPhysicianRepository;
import com.hcl.physician_portal.repository.ISpecimenPickupRequestRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SpecimenPickupRequestService {

    private final ISpecimenPickupRequestRepository iSpecimenPickupRequestRepository;
    private final SpecimenPickupRequestMapper specimenPickupRequestMapper;
    private final IPhysicianRepository iPhysicianRepository;

    public SpecimenPickupRequestService(ISpecimenPickupRequestRepository iSpecimenPickupRequestRepository,
            SpecimenPickupRequestMapper specimenPickupRequestMapper, IPhysicianRepository iPhysicianRepository) {
        this.iSpecimenPickupRequestRepository = iSpecimenPickupRequestRepository;
        this.specimenPickupRequestMapper = specimenPickupRequestMapper;
        this.iPhysicianRepository = iPhysicianRepository;
    }

    public List<ViewSpecimenPickupRequest> addDefaultSpecimenPickupRequests() {
        Physician physician = iPhysicianRepository.findByMobileNumber("9876543210")
                .orElseThrow(() -> new PhysicianNotFoundException("Physician not found"));

        List<SpecimenPickupRequest> requests = new ArrayList<>();

        // Example 1: IN_OFFICE, closure time, scheduled date, pickup address,
        // temperature info, Scheduled Date is current Date
        SpecimenPickupRequest req1 = new SpecimenPickupRequest();
        req1.setScheduledDate(java.time.LocalDateTime.now());
        // req1.setPickupAddress("Podalakur, SPSR Nellore district, Andhra Pradesh,
        // 524345, India");
        req1.setPickupAddress("Rio Hospital Madurai, Madurai");
        req1.setTemperatureInfo(TemperatureInfo.AMBIENT);
        req1.setPickupType(com.hcl.physician_portal.Enum.PickupType.IN_OFFICE);
        req1.setClosureTime(java.time.LocalDateTime.now().plusHours(8).withMinute(0));
        req1.setRouteID("44fcf11a-a688-3f35-8379-14b774db03fc");
        req1.setStatus(com.hcl.physician_portal.Enum.PickupStatus.CREATED);
        req1.setPhysician(physician);
        req1.setCreatedAt(java.time.LocalDateTime.now());
        req1.setUpdatedAt(java.time.LocalDateTime.now());
        requests.add(req1);

        // Example 2: LOCKBOX, pickup request time before - Scheduled Date is 2 days
        SpecimenPickupRequest req2 = new SpecimenPickupRequest();
        req2.setScheduledDate(java.time.LocalDateTime.now());
        // req2.setPickupAddress("Manubolu, SPSR Nellore district, Andhra Pradesh,
        // 524405, India");
        req2.setPickupAddress("Vikram Multispeciality Hospital Madurai, Madurai");
        req2.setTemperatureInfo(TemperatureInfo.FROZEN);
        req2.setPickupType(com.hcl.physician_portal.Enum.PickupType.LOCKBOX);
        req2.setPickupRequestTimeBefore(java.time.LocalDateTime.now().plusHours(5).withMinute(0));
        req2.setRouteID("44fcf11a-a688-3f35-8379-14b774db03fc");
        req2.setStatus(com.hcl.physician_portal.Enum.PickupStatus.CREATED);
        req2.setPhysician(physician);
        req2.setCreatedAt(java.time.LocalDateTime.now());
        req2.setUpdatedAt(java.time.LocalDateTime.now());
        requests.add(req2);

        // Example 3: LOCKBOX, pickup request time after - Scheduled Date is current
        // Date
        SpecimenPickupRequest req3 = new SpecimenPickupRequest();
        req3.setScheduledDate(java.time.LocalDateTime.now());
        // req3.setPickupAddress("Madamanuru, SPSR Nellore district, Andhra Pradesh,
        // 524405, India");
        req3.setRouteID("00bc9352-2197-3296-93b3-4a8455cbb04b");
        req3.setPickupAddress("Madurai Junction, Madurai");
        req3.setTemperatureInfo(com.hcl.physician_portal.Enum.TemperatureInfo.FROZEN);
        req3.setPickupType(com.hcl.physician_portal.Enum.PickupType.LOCKBOX);
        req3.setPickupRequestTimeAfter(java.time.LocalDateTime.now().plusHours(3).withMinute(0));
        req3.setStatus(com.hcl.physician_portal.Enum.PickupStatus.CREATED);
        req3.setPhysician(physician);
        req3.setCreatedAt(java.time.LocalDateTime.now());
        req3.setUpdatedAt(java.time.LocalDateTime.now());
        requests.add(req3);

        // Example 4: IN_OFFICE, closure time, scheduled date, pickup address,
        // temperature info - Scheduled Date is 2 days after current Date
        SpecimenPickupRequest req4 = new SpecimenPickupRequest();
        req4.setScheduledDate(java.time.LocalDateTime.now().plusDays(2));
        // req4.setPickupAddress("Podalakur, SPSR Nellore district, Andhra Pradesh,
        // 524345, India");
        req4.setPickupAddress("Vikram Multispeciality Hospital Madurai, Madurai");
        req4.setTemperatureInfo(TemperatureInfo.AMBIENT);
        req4.setPickupType(com.hcl.physician_portal.Enum.PickupType.IN_OFFICE);
        req4.setClosureTime(java.time.LocalDateTime.now().plusDays(2).plusHours(8).withMinute(0));
        req4.setRouteID("44fcf11a-a688-3f35-8379-14b774db03fc");
        req4.setStatus(com.hcl.physician_portal.Enum.PickupStatus.CREATED);
        req4.setPhysician(physician);
        req4.setCreatedAt(java.time.LocalDateTime.now());
        req4.setUpdatedAt(java.time.LocalDateTime.now());
        requests.add(req4);

        // Example 5: LOCKBOX, pickup request time after - Scheduled Date is 3 days
        // current Date
        SpecimenPickupRequest req5 = new SpecimenPickupRequest();
        req5.setScheduledDate(java.time.LocalDateTime.now().plusDays(3));
        // req5.setPickupAddress("Madamanuru, SPSR Nellore district, Andhra Pradesh,
        // 524405, India");
        req5.setRouteID("00bc9352-2197-3296-93b3-4a8455cbb04b");
        req5.setPickupAddress("Madurai Junction, Madurai");
        req5.setTemperatureInfo(com.hcl.physician_portal.Enum.TemperatureInfo.FROZEN);
        req5.setPickupType(com.hcl.physician_portal.Enum.PickupType.LOCKBOX);
        req5.setPickupRequestTimeAfter(java.time.LocalDateTime.now().plusDays(3).withHour(15).withMinute(0));
        req5.setStatus(com.hcl.physician_portal.Enum.PickupStatus.CREATED);
        req5.setPhysician(physician);
        req5.setCreatedAt(java.time.LocalDateTime.now());
        req5.setUpdatedAt(java.time.LocalDateTime.now());
        requests.add(req5);

        // // Example 4: IN_OFFICE, closure time, scheduled date, pickup address,
        // // temperature info
        // SpecimenPickupRequest req4 = new SpecimenPickupRequest();
        // req4.setScheduledDate(java.time.LocalDateTime.now().plusDays(4));
        // req4.setPickupAddress("Akkampeta, SPSR Nellore district, Andhra Pradesh,
        // India");
        // req4.setTemperatureInfo(TemperatureInfo.REFRIGERATED);
        // req4.setPickupType(com.hcl.physician_portal.Enum.PickupType.IN_OFFICE);
        // req4.setClosureTime(java.time.LocalDateTime.now().plusDays(4).withHour(18).withMinute(0));
        // req4.setStatus(com.hcl.physician_portal.Enum.PickupStatus.CREATED);
        // req4.setCreatedAt(java.time.LocalDateTime.now());
        // req4.setUpdatedAt(java.time.LocalDateTime.now());
        // requests.add(req4);

        // Save all requests
        return specimenPickupRequestMapper
                .specimenPickupRequestListToViewList(iSpecimenPickupRequestRepository.saveAll(requests));
    }

    public ViewSpecimenPickupRequest createSpecimenPickupRequest(SpecimenPickupRequestDTO specimenPickupRequestDTO) {

        // Need to fetch from the JWT token details
        Physician physician = iPhysicianRepository.findByMobileNumber("9876543210")
                .orElseThrow(() -> new PhysicianNotFoundException("Physician not found"));
        SpecimenPickupRequest specimenPickupRequest = specimenPickupRequestMapper
                .specimenPickupRequestDTOToEntity(specimenPickupRequestDTO);
        specimenPickupRequest.setPhysician(physician);

        return specimenPickupRequestMapper.specimenPickupRequestEntityToView(iSpecimenPickupRequestRepository
                .save(specimenPickupRequest));
    }

    public ViewSpecimenPickupRequest getSpecimenRequestById(UUID id) {
        // also add to check if the request id is created physician is same as that of
        // the one who is hiting the API(JWT user == req.getPhysicianID())
        SpecimenPickupRequest req = iSpecimenPickupRequestRepository.findById(id).orElse(null);
        if (req != null) {
            return specimenPickupRequestMapper.specimenPickupRequestEntityToView(req);
        }
        return null;
    }

    public List<ViewSpecimenPickupRequest> getAllSpecimenRequests() {
        Physician physician = iPhysicianRepository.findByMobileNumber("9876543210")
                .orElseThrow(() -> new PhysicianNotFoundException("Physician not found"));

        List<SpecimenPickupRequest> entities = iSpecimenPickupRequestRepository
                .findAllRequestsByPhysician(physician.getId());

        List<ViewSpecimenPickupRequest> viewSpecimenPickupRequests = new ArrayList<>();
        for (SpecimenPickupRequest req : entities) {
            viewSpecimenPickupRequests.add(specimenPickupRequestMapper.specimenPickupRequestEntityToView(req));
        }
        return viewSpecimenPickupRequests;
    }

    public ViewSpecimenPickupRequest updateSpecimenRequestStatus(UUID id, String status) {
        // also add to check if the request id is created physician is same as that of
        // the one who is hiting the API

        SpecimenPickupRequest req = iSpecimenPickupRequestRepository.findById(id).orElse(null);
        if (req != null) {
            try {
                req.setStatus(com.hcl.physician_portal.Enum.PickupStatus.valueOf(status));
                SpecimenPickupRequest saved = iSpecimenPickupRequestRepository.save(req);
                return specimenPickupRequestMapper.specimenPickupRequestEntityToView(saved);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }
}