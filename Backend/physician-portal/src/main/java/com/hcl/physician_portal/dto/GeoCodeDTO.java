package com.hcl.physician_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GeoCodeDTO {
        @JsonProperty("latitude")
        double latitude;

        @JsonProperty("longitude")
        double longitude;

        @JsonProperty("display_name")
        String displayName;
}