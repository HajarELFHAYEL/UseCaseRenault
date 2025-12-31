package com.renault.garage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lorsqu'un garage n'est pas trouvé.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class GarageNotFoundException extends RuntimeException {
    
    public GarageNotFoundException(Long id) {
        super("Garage non trouvé avec l'ID: " + id);
    }
}
