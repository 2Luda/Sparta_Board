package com.sparta.sparta_board.dto;

import com.sparta.sparta_board.entity.Board;
import com.sparta.sparta_board.entity.User;
import com.sparta.sparta_board.repository.UserRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.dialect.PostgreSQLDialect;

@NoArgsConstructor
@Getter
public class BoardRequestDto {

    // board에 데이터를 넣을 때의 입력 요청 값을 받음.
    // 1. annotation 붙이기
    // 2. 필드 정보 넣기

    // 글 제목
    private String title;

    // 글 내용
    private String content;


}
