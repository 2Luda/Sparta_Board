package com.sparta.sparta_board.exception;

public class UserException extends BaseException {

    // BaseExceptionType을 멤버변수로 가지고 있으며, 생성자를 통해 생성하는 순간 ExceptionType을 설정하도록 만들어주었습니다.
    private BaseExceptionType exceptionType;

    public UserException(BaseExceptionType exceptionType) {
        this.exceptionType= exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
