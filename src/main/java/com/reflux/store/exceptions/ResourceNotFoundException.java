package com.reflux.store.exceptions;

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

    public ResourceNotFoundException(String message, String resource, String field, Long fieldId) {
        super(String.format("%s not found with %s: %d", resource, field, fieldId));
        this.resource = resource;
        this.field = field;
        this.fieldId = fieldId;
    }

    public ResourceNotFoundException() {}
}
