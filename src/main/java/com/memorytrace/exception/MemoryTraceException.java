package com.memorytrace.exception;

public class MemoryTraceException extends RuntimeException {

    public MemoryTraceException() {
        super();
    }

    public MemoryTraceException(String message) {
        super(message);
    }

    public MemoryTraceException(Throwable ex) {
        super(ex);
    }

    public MemoryTraceException(String message, Throwable ex) {
        super(message, ex);
    }
}
