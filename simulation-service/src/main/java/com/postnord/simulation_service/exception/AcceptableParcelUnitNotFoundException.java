package com.postnord.simulation_service.exception;



public class AcceptableParcelUnitNotFoundException extends RuntimeException {
    public AcceptableParcelUnitNotFoundException(Long experimentId) {
        super("No acceptable parcel unit found for experiment: " + experimentId);
    }
}