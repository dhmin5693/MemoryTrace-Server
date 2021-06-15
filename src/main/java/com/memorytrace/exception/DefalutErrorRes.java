package com.memorytrace.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class DefalutErrorRes {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private int statusCode;
    private String responseMessage;

    public DefalutErrorRes(LocalDateTime timestamp, int statusCode, String responseMessage) {
        this.timestamp = timestamp;
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
    }
}
