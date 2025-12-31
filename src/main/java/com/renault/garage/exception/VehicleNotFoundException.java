package com.renault.garage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lorsqu'un véhicule n'est pas trouvé.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class VehicleNotFoundException extends RuntimeException {
    
    public VehicleNotFoundException(Long id) {
        super("Véhicule non trouvé avec l'ID: " + id);
    }
}
