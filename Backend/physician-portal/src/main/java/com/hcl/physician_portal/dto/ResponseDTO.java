package com.hcl.physician_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ResponseDTO<T> {
    private T data;
    private String message;
}