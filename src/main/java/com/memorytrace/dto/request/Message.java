package com.memorytrace.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "Fcm 알람 요청")
public class Message {

    private String subject;
    private String content;

    @Builder
    public Message(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }
}
