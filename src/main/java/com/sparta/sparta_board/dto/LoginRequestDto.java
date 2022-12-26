package com.sparta.sparta_board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    // @Getter, @Setter 어노테이션을 추가한다.
    // 로그인은 닉네임과 비밀번호만 받는다.
    private String username;
    private String password;

}
