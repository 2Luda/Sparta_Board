package com.sparta.sparta_board.exception;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {

    // 에러코드와 Http 상태, 그리고 에러 메세지를 가지고 있도록 만들어 주기 위해 다음과 같이 Getter 메소드를 설정
    int getErrorCode();
    HttpStatus gettHttpStatus();
    String getErrorMessage();
}
