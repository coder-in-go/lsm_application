package com.hcl.physician_portal.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.hcl.physician_portal.dto.GeoCodeDTO;

import java.security.MessageDigest;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GeoCodeHelper {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final double GRID_SIZE = 0.03;

    public GeoCodeDTO geocodeLatitudeLongitude(String address) {
        try {
            String url = "https://nominatim.openstreetmap.org/search?q=" + address.replace(" ", "+")
                    + "&format=json&limit=5";

            System.out.println("url----------------------------------->" + url);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JsonNode arr = objectMapper.readTree(response.getBody());
            if (arr.isArray() && arr.size() > 0) {
                // Pick the best match (first result)
                JsonNode obj = arr.get(0);
                GeoCodeDTO geoCodeDTO = new GeoCodeDTO();
                geoCodeDTO.setLatitude(obj.get("lat").asDouble());
                geoCodeDTO.setLongitude(obj.get("lon").asDouble());
                geoCodeDTO.setDisplayName(obj.has("display_name") ? obj.get("display_name").asText() : "");
                return geoCodeDTO;
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public String getETA(String origin, String destination) {
        try {
            GeoCodeDTO originResult = geocodeLatitudeLongitude(origin);
            GeoCodeDTO destResult = geocodeLatitudeLongitude(destination);

            if (originResult == null || destResult == null) {
                return "Unable to geocode one or both addresses.\n" +
                        (originResult == null ? "Origin: " + origin + "\n" : "") +
                        (destResult == null ? "Destination: " + destination + "\n" : "");
            }

            // OSRM route request
            String osrmUrl = String.format(
                    "http://router.project-osrm.org/route/v1/driving/%f,%f;%f,%f?overview=false",
                    originResult.getLongitude(), originResult.getLatitude(), destResult.getLongitude(),
                    destResult.getLatitude());

            ResponseEntity<String> response = restTemplate.getForEntity(osrmUrl, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());

            if (root.has("routes") && root.get("routes").size() > 0) {
                double duration = root.get("routes").get(0).get("duration").asDouble();
                long minutes = Math.round(duration / 60);
                return Long.toString(minutes);
            }

            return "No route found.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String generateRouteId(String origin, String destination) {
        try {
            GeoCodeDTO originResult = geocodeLatitudeLongitude(origin);
            GeoCodeDTO destResult = geocodeLatitudeLongitude(destination);

            if (originResult == null || destResult == null) {
                return "Unable to geocode one or both addresses.\n" +
                        (originResult == null ? "Origin: " + origin + "\n" : "") +
                        (destResult == null ? "Destination: " + destination + "\n" : "");
            }

            double distance = calculateDistance(
                    originResult.getLatitude(), originResult.getLongitude(),
                    destResult.getLatitude(), destResult.getLongitude());

            if (distance <= 3.0) {
                // Same route ID for nearby locations
                String sharedKey = Stream.of(originResult, destResult)
                        .sorted(Comparator.comparing(GeoCodeDTO::getLatitude)
                                .thenComparing(GeoCodeDTO::getLongitude))
                        .map(g -> g.getLatitude() + "," + g.getLongitude())
                        .collect(Collectors.joining("|"));
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++" +
                        sharedKey);
                return hashRoute(sharedKey);
            } else {
                // Unique route ID
                String routeKey = originResult.getLatitude() + "," +
                        originResult.getLongitude() + "|" +
                        destResult.getLatitude() + "," + destResult.getLongitude();
                return hashRoute(routeKey);
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String hashRoute(String routeData) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(routeData.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString().substring(0, 16); // short routeId
    }

    // public List<String> getOptimizedRouteOrder(String origin, List<String>
    // destinations) {
    // try {
    // GeoCodeDTO originResult = geocodeLatitudeLongitude(origin);
    // if (originResult == null) {
    // throw new RuntimeException("Origin address could not be geocoded: " +
    // origin);
    // }

    // for (String destination : destinations) {
    // GeoCodeDTO destResult = geocodeLatitudeLongitude(destination);
    // if (destResult == null) {
    // throw new RuntimeException("Destination address could not be geocoded: " +
    // destination);
    // }

    // // if (originResult != null || destResult != null) {
    // String osrmUrl = String.format(
    // "http://router.project-osrm.org/route/v1/driving/%f,%f;%f,%f?overview=false",
    // originResult.getLongitude(), originResult.getLatitude(),
    // destResult.getLongitude(),
    // destResult.getLatitude());

    // ResponseEntity<String> response = restTemplate.getForEntity(osrmUrl,
    // String.class);
    // JsonNode root = objectMapper.readTree(response.getBody());

    // if (root.has("waypoints")) {
    // List<String> orderedAddresses = new ArrayList<>();
    // JsonNode waypoints = root.get("waypoints");
    // for (int i = 1; i < waypoints.size(); i++) { // skip origin
    // orderedAddresses.add(destinations.get(i - 1));
    // }
    // return orderedAddresses;
    // }
    // // }
    // }
    // return destinations; // fallback if no optimized order found
    // } catch (Exception e) {
    // System.out.println("Error occurred while optimizing route: " +
    // e.getMessage());
    // return destinations;
    // }
    // }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371; // Radius of the Earth in kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c; // Distance in kilometers
    }

    private String getGridKey(double latitude, double longitude) {
        int latBlock = (int) Math.round(latitude / GRID_SIZE);
        int lonBlock = (int) Math.round(longitude / GRID_SIZE);
        return latBlock + "_" + lonBlock;
    }

    public String generateRouteIdFromGrid(double latitude, double longitude) {
        String gridKey = getGridKey(latitude, longitude);
        return UUID.nameUUIDFromBytes(gridKey.getBytes()).toString();
    }

}