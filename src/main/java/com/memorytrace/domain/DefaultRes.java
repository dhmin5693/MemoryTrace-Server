package com.memorytrace.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DefaultRes<T> {

    @ApiModelProperty(position = 1, required = true, value = "응답 상태 코드")
    private int statusCode;

    @ApiModelProperty(position = 2, required = true, value = "응답 메시지")
    private String responseMessage;

    @ApiModelProperty(position = 3, required = true, value = "데이터")
    private T data;

    public DefaultRes(final int statusCode, final String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.data = null;
    }

    public static <T> DefaultRes<T> res(final int statusCode, final String responseMessage) {
        return res(statusCode, responseMessage, null);
    }

    public static <T> DefaultRes<T> res(final int statusCode, final String responseMessage,
        final T t) {
        return DefaultRes.<T>builder()
            .data(t)
            .statusCode(statusCode)
            .responseMessage(responseMessage)
            .build();
    }
}
