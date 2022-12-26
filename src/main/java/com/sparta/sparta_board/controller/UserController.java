package com.sparta.sparta_board.controller;

import com.sparta.sparta_board.dto.LoginRequestDto;
import com.sparta.sparta_board.dto.SignupRequestDto;
import com.sparta.sparta_board.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

//    // 회원가입 페이지로 이동
//    @GetMapping("/signup")
//    public ModelAndView signupPage() {
//        return new ModelAndView("signup");
//    }
//
//    // 로그인 페이지로 이동
//    @GetMapping("/login")
//    public ModelAndView loginPage() {
//        return new ModelAndView("login");
//    }

    // 회원가입 기능
    @PostMapping("/signup")
    public String signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    // 로그인 기능
    @ResponseBody
    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
       return userService.login(loginRequestDto,response);
    }


}
