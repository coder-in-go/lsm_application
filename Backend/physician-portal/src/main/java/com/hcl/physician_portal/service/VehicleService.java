package com.hcl.physician_portal.service;

import com.hcl.physician_portal.Enum.PickupStatus;
import com.hcl.physician_portal.Enum.VehicleStatus;
import com.hcl.physician_portal.dto.GeoCodeDTO;
import com.hcl.physician_portal.dto.VehicleAssignmentResponse;
import com.hcl.physician_portal.dto.ViewVehicle;
import com.hcl.physician_portal.exception.SpecimenRequestNotFoundException;
import com.hcl.physician_portal.exception.VehicleNotFoundException;
import com.hcl.physician_portal.helpers.GeoCodeHelper;
import com.hcl.physician_portal.model.SpecimenPickupRequest;
import com.hcl.physician_portal.model.Vehicle;
import com.hcl.physician_portal.repository.ISpecimenPickupRequestRepository;
import com.hcl.physician_portal.repository.IVehicleRepository;

import jakarta.transaction.Transactional;

import com.hcl.physician_portal.mapper.VehicleMapper;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VehicleService {
    private final ISpecimenPickupRequestRepository specimenRepo;
    private VehicleMapper vehicleMapper;
    private final IVehicleRepository iVehicleRepository;
    private final GeoCodeHelper geoCodeHelper;
    private final TimerNotificationService timerNotificationService;

    public VehicleService(IVehicleRepository iVehicleRepository, ISpecimenPickupRequestRepository specimenRepo,
            VehicleMapper vehicleMapper, GeoCodeHelper geoCodeHelper,
            TimerNotificationService timerNotificationService) {
        this.iVehicleRepository = iVehicleRepository;
        this.specimenRepo = specimenRepo;
        this.vehicleMapper = vehicleMapper;
        this.geoCodeHelper = geoCodeHelper;
        this.timerNotificationService = timerNotificationService;
    }

    public String addDefaultVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        Vehicle vehicle1 = new Vehicle();
        Vehicle vehicle2 = new Vehicle();

        vehicle1.setVehicleId(UUID.fromString("a0572d66-345c-4c10-97e7-d2f9e4f7d887"));
        vehicle1.setVehicleNumber("TN 67 BP 9856");
        vehicle1.setCurrentAddress("Anna Nagar, Madurai, Madurai");
        // vehicle1.setLatitude(9.9325); // Approximate latitude for Anna Nagar, Madurai
        // vehicle1.setLongitude(78.1386); // Approximate longitude
        vehicle1.setStatus(VehicleStatus.AVAILABLE);
        vehicle1.setIsDeleted(false);
        vehicle1.setCreatedAt(LocalDateTime.now());
        vehicle1.setUpdatedAt(LocalDateTime.now());
        vehicles.add(vehicle1);

        vehicle2.setVehicleId(UUID.fromString("b1b76c98-821a-4357-a04e-b6ae3d9a76ec"));
        vehicle2.setVehicleNumber("TN 67 BE 3559");
        vehicle2.setCurrentAddress("Anna Nagar, Madurai, Madurai");
        // vehicle2.setLatitude(9.9325); // Approximate latitude for Anna Nagar, Madurai
        // vehicle2.setLongitude(78.1386); // Approximate longitude
        vehicle2.setStatus(VehicleStatus.AVAILABLE);
        vehicle2.setIsDeleted(false);
        vehicle2.setCreatedAt(LocalDateTime.now());
        vehicle2.setUpdatedAt(LocalDateTime.now());
        vehicles.add(vehicle2);

        iVehicleRepository.saveAll(vehicles);

        return "Added vehicles to DB";
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        return iVehicleRepository.save(vehicle);
    }

    // get vehicle by ID
    public Vehicle getVehicleById(UUID id) {
        return iVehicleRepository.findById(id).orElse(null);
    }

    // get all vehicles
    public List<Vehicle> getAllVehicles() {
        return iVehicleRepository.findAll();
    }

    // Updating vehicle status
    public ViewVehicle updateVehicleStatus(UUID id) {
        Vehicle vehicle = iVehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with ID " + id + " not found"));

        VehicleStatus newStatus = (vehicle.getStatus() == VehicleStatus.AVAILABLE)
                ? VehicleStatus.OCCUPIED
                : VehicleStatus.AVAILABLE;

        vehicle.setStatus(newStatus);
        Vehicle updatedVehicle = iVehicleRepository.save(vehicle);

        return vehicleMapper.vehicleEntityToView(updatedVehicle);
    }

    // delete vehicle
    public boolean deleteVehicle(UUID id) {

        Vehicle entity = iVehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with ID - " + id + " not found"));
        entity.setIsDeleted(true); // or entity.setStatus(Status.INACTIVE);
        entity.setUpdatedAt(LocalDateTime.now());
        iVehicleRepository.save(entity);

        return true;
    }

    @Transactional
    public VehicleAssignmentResponse assignVehicleToSpecimenRequest() {
        double thresholdKm = 3.0;
        List<Vehicle> availableVehicles = iVehicleRepository.findAll().stream()
                .filter(v -> v.getStatus() == VehicleStatus.AVAILABLE)
                .collect(java.util.stream.Collectors.toList());
        if (availableVehicles.isEmpty())
            throw new VehicleNotFoundException("No available vehicles found");

        List<SpecimenPickupRequest> specimens = specimenRepo.findAll().stream()
                .filter(s -> s.getStatus() == PickupStatus.CREATED)
                .collect(java.util.stream.Collectors.toList());
        if (specimens.isEmpty())
            throw new SpecimenRequestNotFoundException("No specimen request is available");

        SpecimenPickupRequest primaryRequest = prioritizeSpecimens(specimens).get(0);
        Vehicle vehicle = availableVehicles.get(0);

        GeoCodeDTO primaryRequestGeoCode = geoCodeHelper.geocodeLatitudeLongitude(primaryRequest.getPickupAddress());

        // String routeId = geoCodeHelper.generateRouteIdFromGrid(
        // primaryRequestGeoCode.getLatitude(),
        // primaryRequestGeoCode.getLongitude());

        // Collect same-route requests
        List<SpecimenPickupRequest> sameRouteRequests = specimens.stream()
                .filter(s -> s.getStatus() == PickupStatus.CREATED &&
                        !s.getId().equals(primaryRequest.getId()) &&
                        s.getScheduledDate() != null &&
                        s.getScheduledDate().toLocalDate().equals(LocalDate.now()) &&
                        geoCodeHelper.calculateDistance(primaryRequestGeoCode.getLatitude(),
                                primaryRequestGeoCode.getLongitude(),
                                geoCodeHelper.geocodeLatitudeLongitude(s.getPickupAddress()).getLatitude(),
                                geoCodeHelper.geocodeLatitudeLongitude(s.getPickupAddress())
                                        .getLongitude()) <= thresholdKm)
                .collect(Collectors.toList());

        // Assign primary request first
        String etaString = geoCodeHelper.getETA(vehicle.getCurrentAddress(), primaryRequest.getPickupAddress());

        primaryRequest.setEstimatedTimeOfArrival(calculateETA(etaString));
        System.out.println("?????????????????????????ETA" + calculateETA(etaString));

        assignRequestToVehicle(primaryRequest, vehicle);

        // Collect all assigned requests
        List<SpecimenPickupRequest> assignedRequests = new ArrayList<>();
        assignedRequests.add(primaryRequest);

        // Assign others in optimized order
        int i = 0;
        for (SpecimenPickupRequest request : sameRouteRequests) {
            if (isTimeWindowCompatible(primaryRequest, request)) {
                String otherEta = geoCodeHelper.getETA(vehicle.getCurrentAddress(), request.getPickupAddress());
                String calculatedOtherETA = calculateETA(otherEta);

                // Parse the string to LocalTime
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime time = LocalTime.parse(calculatedOtherETA, formatter);
                LocalTime updatedTime = time.plusMinutes(i + 7);

                request.setEstimatedTimeOfArrival(updatedTime.format(formatter));

                assignRequestToVehicle(request, vehicle);
                assignedRequests.add(request);
            }
        }

        iVehicleRepository.save(vehicle);
        return vehicleMapper.mapToVehicleAssignmentResponse(vehicle, assignedRequests);
    }

    private void assignRequestToVehicle(SpecimenPickupRequest request, Vehicle vehicle) {
        request.setAssignedVehicle(vehicle);
        request.setStatus(PickupStatus.ASSIGNED);
        specimenRepo.save(request);
        vehicle.setStatus(VehicleStatus.OCCUPIED);

        timerNotificationService.startTimer(request.getId().toString(), Duration.ofMinutes(5));
    }

    private boolean isTimeWindowCompatible(SpecimenPickupRequest primary, SpecimenPickupRequest other) {
        if (primary.getPickupRequestTimeBefore() == null || primary.getPickupRequestTimeAfter() == null ||
                other.getPickupRequestTimeBefore() == null || other.getPickupRequestTimeAfter() == null) {
            // If any time window is missing, treat as compatible (or you can return false
            // if stricter)
            return true;
        }
        return !(other.getPickupRequestTimeBefore().isAfter(primary.getPickupRequestTimeAfter()) ||
                other.getPickupRequestTimeAfter().isBefore(primary.getPickupRequestTimeBefore()));
    }

    private List<SpecimenPickupRequest> prioritizeSpecimens(List<SpecimenPickupRequest> specimens) {
        LocalDate today = LocalDate.now();

        List<SpecimenPickupRequest> todaySpecimens = specimens.stream()
                .filter(s -> s.getScheduledDate() != null && s.getScheduledDate().toLocalDate().equals(today))
                .collect(java.util.stream.Collectors.toList());

        List<SpecimenPickupRequest> otherSpecimens = specimens.stream()
                .filter(s -> s.getScheduledDate() == null || !s.getScheduledDate().toLocalDate().equals(today))
                .collect(java.util.stream.Collectors.toList());

        Comparator<SpecimenPickupRequest> inOfficeComparator = Comparator
                .comparing(s -> s.getPickupType() == com.hcl.physician_portal.Enum.PickupType.IN_OFFICE ? 0 : 1);

        Comparator<SpecimenPickupRequest> timeComparator = Comparator.comparing(SpecimenPickupRequest::getScheduledDate,
                Comparator.nullsLast(Comparator.naturalOrder()));

        Comparator<SpecimenPickupRequest> fullComparator = inOfficeComparator.thenComparing(timeComparator);

        List<SpecimenPickupRequest> prioritized = todaySpecimens.isEmpty() ? otherSpecimens : todaySpecimens;
        return prioritized.stream().sorted(fullComparator).collect(java.util.stream.Collectors.toList());
    }

    private String calculateETA(String etaString) {
        int etaInt = Integer.parseInt(etaString);

        LocalDateTime now = LocalDateTime.now();

        // Duration to reach destination (e.g., 45 minutes)
        Duration travelDuration = Duration.ofMinutes(etaInt);

        // Add extra 10 minutes buffer
        Duration buffer = Duration.ofMinutes(10);

        // Calculate ETA
        LocalDateTime eta = now.plus(travelDuration).plus(buffer);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return eta.format(timeFormatter);
    }
}
