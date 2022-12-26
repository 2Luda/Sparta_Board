package com.sparta.sparta_board.dto;

import com.sparta.sparta_board.entity.Board;
import com.sparta.sparta_board.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponseDto {

    // Board에서 값을 가져올 때 (응답) Board(직접적인 entity) 대신 앞에 서줌.
    // password 같은 정보는 노출시키지 않아도 되기 때문에 필드로 넣지 않음.
    // 1. Board의 필드 정보 가져오기
    // 2. Board를 매개변수로 받아 생성자 만들기

    private String title;

    private String content;

    private String username;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList;

    // board의 정보를 받아 boardResponseDto 생성
    public BoardResponseDto(Board board) {
        List<CommentResponseDto> list = new ArrayList<>();
        this.content = board.getContent();
        this.title= board.getTitle();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt= board.getModifiedAt();
        this.username = board.getUser().getUsername();
        for (Comment comment : board.getComments()) {
            list.add(new CommentResponseDto(comment));
        }
        this.commentList = list;
    }

}
