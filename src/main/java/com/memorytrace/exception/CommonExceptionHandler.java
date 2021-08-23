package com.memorytrace.exception;

import com.memorytrace.common.ResponseMessage;
import com.memorytrace.common.StatusCode;
import java.time.LocalDateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MemoryTraceException.class)
    public ResponseEntity<DefalutErrorRes> handleEntityServerException() {
        return ResponseEntity.status(500).body(
            new DefalutErrorRes(LocalDateTime.now(), StatusCode.INTERNAL_SERVER_ERROR,
                ResponseMessage.INTERNAL_SERVER_ERROR));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request) {

        return ResponseEntity.status(400).body(
            new DefalutErrorRes(LocalDateTime.now(), StatusCode.BAD_REQUEST,
                ex.getBindingResult().getObjectName()));
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
        BindException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request) {

        return ResponseEntity.status(400).body(
            new DefalutErrorRes(LocalDateTime.now(), StatusCode.BAD_REQUEST,
                ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }
}
