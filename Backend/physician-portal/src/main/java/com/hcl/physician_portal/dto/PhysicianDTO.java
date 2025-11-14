package com.hcl.physician_portal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PhysicianDTO {
    @JsonProperty("name")
    private String name;

    @JsonProperty("dob")
    private LocalDate dob;

    @JsonProperty("years_of_experience")
    private int yearsOfExperience;

    @JsonProperty("mobile_number")
    private String mobileNumber;

    @JsonProperty("email_id")
    private String emailID;

    @JsonProperty("workstation_addresses")
    private List<String> workstationAddresses;

}