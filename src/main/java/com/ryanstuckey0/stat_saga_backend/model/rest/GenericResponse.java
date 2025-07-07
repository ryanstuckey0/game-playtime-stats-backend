package com.ryanstuckey0.stat_saga_backend.model.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenericResponse {
    private String message;
    private boolean success;
}
