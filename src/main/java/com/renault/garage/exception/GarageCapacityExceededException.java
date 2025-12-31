package com.renault.garage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lorsqu'un garage a atteint sa capacité maximale de 50 véhicules.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GarageCapacityExceededException extends RuntimeException {
    
    public GarageCapacityExceededException(Long garageId) {
        super("Le garage avec l'ID " + garageId + " a atteint sa capacité maximale de 50 véhicules");
    }
    
    public GarageCapacityExceededException(String message) {
        super(message);
    }
}
