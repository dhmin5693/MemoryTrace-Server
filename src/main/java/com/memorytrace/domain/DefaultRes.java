package com.memorytrace.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

// FEEDBACK 약어는 가급적 피하는게 좋습니다.
// res, cnt 등은 일반적인 경우라서 파악하기는 쉽지만 그런 케이스가 아니라면 코드를 읽기가 힘들어져요.
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
