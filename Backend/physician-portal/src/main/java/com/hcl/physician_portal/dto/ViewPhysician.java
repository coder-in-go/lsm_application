package com.hcl.physician_portal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewPhysician {
    @JsonProperty("name")
    private String name;

    @JsonProperty("dob")
    private LocalDate dob;

    @JsonProperty("years_of_experience")
    private int yearsOfExperience;

    @JsonProperty("address")
    private String address;

    @JsonProperty("landmark")
    private String landmark;

    @JsonProperty("workstations")
    private List<ViewWorkstation> viewWorkstationList;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}