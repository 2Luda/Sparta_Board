package com.sparta.sparta_board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
public class SignupRequestDto {
    // 회원가입에 필요한 정보 요청 클래스
    // @Getter , @Setter 어노테이션을 사용한다.
    // 회원가입을 할 때, 닉네임과 패스워드, adminToken 을 받는다.
    // admin 기본 설정은 false 로 설정되어 있다.

    // NotBlank는 "" 같은 공백도 통과를 시키지 않는다. 또한 String만 받을 수가 있다.
    @NotBlank

    // Size는 username의 최소, 최대 사이즈를 지정할 수 있으며 해당 사이즈에 올바르지 않는경우 massage를 담아 예외를 던진다.
    @Size(min = 4, max = 10, message = "이름은 4 ~ 10자 이어야 합니다!")

    // Pattern = 지정한 정규과 대응되는 문자열이어야 한다. Require : String regexp => 정규식 문자열을 지정한다
    @Pattern(regexp = "[a-zA-Z0-9]*$")
    private String username;

    // NotBlank는 "" 같은 공백도 통과를 시키지 않는다. 또한 String만 받을 수가 있다.
    @NotBlank

    // Size는 password의 최소, 최대 사이즈를 지정할 수 있으며 해당 사이즈에 올바르지 않는경우 message를 담아 예외를 던진다.
    @Size(min = 8, max = 15, message = "비밀번호는 8 ~ 15자 이어야 합니다!")

    // Pattern = 지정한 정규과 대응되는 문자열이어야 한다. Require : String regexp => 정규식 문자열을 지정한다
    @Pattern(regexp = "[a-zA-Z0-9]*$")
    private String password;

    private boolean admin = false;

    private String adminToken = "";


}
