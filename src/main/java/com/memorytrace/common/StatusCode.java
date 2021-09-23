package com.memorytrace.common;

// FEEDBACK
// org.springframework.http.HttpStatus, org.apache.http.HttpStatus
// 위 코드에 이미 정의되어 있는 내용이고 이미 이 프로젝트에서도 사용중인 클래스인데
// 굳이 새로 만들 필요는 없는 것 같습니다.
public class StatusCode {

    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_SERVER_ERROR = 500;
}
