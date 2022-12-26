package com.sparta.sparta_board.controller;

import com.sparta.sparta_board.dto.CommentRequestDto;
import com.sparta.sparta_board.dto.CommentResponseDto;
import com.sparta.sparta_board.entity.User;
import com.sparta.sparta_board.exception.UserException;
import com.sparta.sparta_board.exception.UserExceptionType;
import com.sparta.sparta_board.jwt.JwtUtil;
import com.sparta.sparta_board.service.CommentService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    // 댓글 등록 PostMapping 어노테이션을 사용한다.
    // requestDto 와 id(PathVariable로 경로의 {}에 해당하는 id값을 가져온다.)를 매개변수로 받는다.
    @PostMapping("/api/boards/{id}/comment")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long id, HttpServletRequest request) {
        // Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 토큰 가져오기
        if (token != null) {
            if (jwtUtil.vaildateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new UserException(UserExceptionType.NOT_REQUEST_OR_NOT_NORMAL_TOKEN);
            }
            return commentService.createComment(requestDto, id, claims);
        } else {
            throw new UserException(UserExceptionType.NOT_REQUEST_OR_NOT_NORMAL_TOKEN);
        }
    }

    // 댓글 수정
    @PutMapping("/api/boards/{id}/comment")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.vaildateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new UserException(UserExceptionType.NOT_REQUEST_OR_NOT_NORMAL_TOKEN);
            }
            return commentService.update(id,requestDto,claims);
        } else {
            throw new UserException(UserExceptionType.NOT_REQUEST_OR_NOT_NORMAL_TOKEN);
        }
    }

    // 댓글 삭제
    @DeleteMapping("/api/boards/{id}/comment")
    public void deleteComment(@PathVariable Long id, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.vaildateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new UserException(UserExceptionType.NOT_REQUEST_OR_NOT_NORMAL_TOKEN);
            }
            commentService.delete(id,claims);
        }
    }
}
