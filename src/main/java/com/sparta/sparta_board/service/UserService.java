package com.sparta.sparta_board.service;

import com.sparta.sparta_board.dto.LoginRequestDto;
import com.sparta.sparta_board.dto.SignupRequestDto;
import com.sparta.sparta_board.entity.User;
import com.sparta.sparta_board.entity.UserRoleEnum;
import com.sparta.sparta_board.exception.UserException;
import com.sparta.sparta_board.exception.UserExceptionType;
import com.sparta.sparta_board.jwt.JwtUtil;
import com.sparta.sparta_board.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    // 회원가입 서비스
    public String signup(SignupRequestDto signupRequestDto){
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if(found.isPresent()) {
            throw new UserException(UserExceptionType.ALREADY_EXIST_USERNAME);
        }
        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if(signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
            User user = new User(username,password,role);
            userRepository.save(user);
            return "회원가입 성공";
    }

    // 로그인 서비스
    @Transactional(readOnly = true)
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserException(UserExceptionType.LOGIN_NOT_EQUALS_USERNAME_PASSWORD)
        );
        // 비밀번호 확인
        if(!user.getPassword().equals(password)) {
            throw new UserException(UserExceptionType.LOGIN_NOT_EQUALS_USERNAME_PASSWORD);
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));

        return "로그인 완료";
    }
}
