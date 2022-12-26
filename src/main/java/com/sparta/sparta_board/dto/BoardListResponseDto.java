package com.sparta.sparta_board.dto;

import com.sparta.sparta_board.entity.Board;
import com.sparta.sparta_board.entity.Comment;
import com.sparta.sparta_board.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardListResponseDto {

    // 게시판 전채 목록 조회 시 넘겨줄 Response

    // 게시글 제목
    private String title;

    // 닉네임, 시간 ( 오름차순, 내림차순 )
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String content;

    private List<CommentResponseDto> commentsList;


    // Entity - > Dto
    public BoardListResponseDto(Board board) {
        List<CommentResponseDto> list = new ArrayList<>();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.username = board.getUser().getUsername();
        for (Comment comment : board.getComments()) {
            list.add(new CommentResponseDto(comment));
        }
        this.commentsList = list;
    }

}
