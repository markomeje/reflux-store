package com.reflux.store.exception;

public class ResourceNotFoundException extends RuntimeException {

    String resource;
    String field;
    String fieldName;
    Long fieldId;

    public ResourceNotFoundException(String resource, String field, String fieldName) {
        super(resource + " not found with " + field + ": " + fieldName);
        this.resource = resource;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException() {}
}
