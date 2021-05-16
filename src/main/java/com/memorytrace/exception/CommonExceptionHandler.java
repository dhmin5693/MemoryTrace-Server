package com.memorytrace.exception;

import com.memorytrace.common.ResponseMessage;
import com.memorytrace.common.StatusCode;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<DefalutErrorRes> handleEntityServerException() {
        return ResponseEntity.status(500).body(
            new DefalutErrorRes(LocalDateTime.now(), StatusCode.INTERNAL_SERVER_ERROR,
                ResponseMessage.INTERNAL_SERVER_ERROR));
    }
}
