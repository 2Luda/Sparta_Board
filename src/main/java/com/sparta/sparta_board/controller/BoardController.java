package com.sparta.sparta_board.controller;

import com.sparta.sparta_board.dto.BoardListResponseDto;
import com.sparta.sparta_board.dto.BoardRequestDto;
import com.sparta.sparta_board.dto.BoardResponseDto;
import com.sparta.sparta_board.exception.UserException;
import com.sparta.sparta_board.exception.UserExceptionType;
import com.sparta.sparta_board.jwt.JwtUtil;
import com.sparta.sparta_board.service.BoardService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // JSON으로 데이터를 주고받음을 선언합니다.
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    // 1. anootation 붙이기
    // 2. boardService 가져오고, 생성자 생성
    // 3. 알맞은 Mapping 으로 생성
    private final BoardService boardService;
    private final JwtUtil jwtUtil;

    // 글 등록 POST 사용
    // BoardResponseDto로 리턴 타입 설정, 매개변수로 BoardRequestDto를 받는다.
    @PostMapping("/boards")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        // Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        // 토큰이 있는 경우에만 토큰 가져오기
       if (token != null) {
           if (jwtUtil.vaildateToken(token)) {
               claims = jwtUtil.getUserInfoFromToken(token);
           }else {
               throw new UserException(UserExceptionType.NOT_REQUEST_OR_NOT_NORMAL_TOKEN);
           }
       return boardService.createBoard(requestDto,claims);
       }else {
           throw new UserException(UserExceptionType.NOT_REQUEST_OR_NOT_NORMAL_TOKEN);
       }
    }

    // 전체 목록 조회 GET 사용
    @GetMapping("/boards")
    public List<BoardListResponseDto> getAllBoards() {

        return boardService.findAllBoard();
    }

    // 게시글 선택 목록 조회 GET 사용
    @GetMapping("/boards/{id}")
    public BoardResponseDto getOneBoard(@PathVariable Long id) {
        return boardService.findOneBoard(id);
    }

    // 게시글 수정 기능 PUT 기능 사용
    @PutMapping("/boards/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null){
            if (jwtUtil.vaildateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            }else {
                throw new UserException(UserExceptionType.NOT_REQUEST_OR_NOT_NORMAL_TOKEN);
            }
            return boardService.update(id,requestDto,claims);
        } else {
            throw new UserException(UserExceptionType.NOT_REQUEST_OR_NOT_NORMAL_TOKEN);
        }
    }

    // 게시글 삭제 기능 DELETE 기능 사용
    @DeleteMapping("/boards/{id}")
    public void deleteBoard(@PathVariable Long id , HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.vaildateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new UserException(UserExceptionType.NOT_REQUEST_OR_NOT_NORMAL_TOKEN);
            }
            boardService.delete(id,claims);
        }
    }







}
