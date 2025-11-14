package com.hcl.physician_portal.dto;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {
    @JsonProperty("message")
    private String mesaage;

    @JsonProperty("time_stamp")
    private String timestamp;
}