package com.memorytrace.dto.request;

import com.memorytrace.domain.Book;
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
    private Book data;

    @Builder
    public Message(String subject, String content, Book data) {
        this.subject = subject;
        this.content = content;
        this.data = data;
    }
}
