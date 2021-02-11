package com.robothy.exunion.core.exception;

public class ServiceNotFoundException extends RuntimeException {

    public ServiceNotFoundException(String message) {
        super(message);
    }

    public ServiceNotFoundException(Class<?> clazz) {
        super("No implementation found for " + clazz.getName());
    }

}
