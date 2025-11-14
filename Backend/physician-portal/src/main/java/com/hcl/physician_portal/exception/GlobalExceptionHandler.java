package com.hcl.physician_portal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.hcl.physician_portal.dto.ErrorDTO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        String message = "Validation failed: " + errors.toString();
        ErrorDTO errorDTO = new ErrorDTO(message, LocalDateTime.now().toString());

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SpecimenRequestNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleSpecimenRequestNotFound(SpecimenRequestNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage(), LocalDateTime.now().toString());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleVehicleNotFound(VehicleNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage(), LocalDateTime.now().toString());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage(), LocalDateTime.now().toString());
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
