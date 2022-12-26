package com.sparta.sparta_board.exception;

import org.springframework.http.HttpStatus;

public enum UserExceptionType implements BaseExceptionType{
    // 회원가입, 로그인 시
    //에러코드와 Http 상태코드, 그리고 에러 메세지가 존재합니다.
    ALREADY_EXIST_USERNAME(400, HttpStatus.BAD_REQUEST, "이미 존재하는 Username입니다."),
    LOGIN_NOT_EQUALS_USERNAME_PASSWORD(400,HttpStatus.BAD_REQUEST,"회원을 찾을 수 없습니다"),
    ALREADY_TOKEN_NOT_POST_COMMENT(400, HttpStatus.BAD_REQUEST,"작성자만 삭제/수정할 수 있습니다."),
    NOT_REQUEST_OR_NOT_NORMAL_TOKEN(400,HttpStatus.BAD_REQUEST,"토큰이 유효하지 않습니다.");

    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    UserExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public HttpStatus gettHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
