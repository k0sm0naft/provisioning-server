package com.voxloud.provisioning.exception;

public class FormatConfigException extends RuntimeException {
    public FormatConfigException(String message, Exception exception) {
        super(message, exception);
    }
}
