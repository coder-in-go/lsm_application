package com.hcl.physician_portal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ViewWorkstation {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("address")
    private String address;

    @JsonProperty("landmark")
    private String landmark;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}