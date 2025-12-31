package com.renault.garage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lorsqu'un accessoire n'est pas trouvé.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccessoryNotFoundException extends RuntimeException {
    
    public AccessoryNotFoundException(Long id) {
        super("Accessoire non trouvé avec l'ID: " + id);
    }
}
