package com.voxloud.provisioning.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
          .getFieldErrors()
          .forEach(error ->
                  errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<String> handleDeviceNotFound(DeviceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UnsupportedDeviceTypeException.class)
    public ResponseEntity<String> handleUnsupportedDeviceType(UnsupportedDeviceTypeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(ex.getMessage());
    }

    @ExceptionHandler({FormatConfigException.class, ParseOverrideFragmentException.class})
    public ResponseEntity<String> handleUnsupportedDeviceType(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(ex.getMessage());
    }

    @ExceptionHandler({
            FormatConfigException.class,
            ParseOverrideFragmentException.class,
            Exception.class
    })
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
