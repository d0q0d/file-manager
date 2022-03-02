package com.log75.github.service.exceptions;

import com.vasl.connect.utils.crud.api.InternationalizeConfig;

public class FileUploadFailedException extends RuntimeException {
    private String message;

    public FileUploadFailedException() {
    }
    public FileUploadFailedException(String message) {
        super(message);
        this.message = message;
    }
    public FileUploadFailedException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

}
