package com.sparta.sparta_board.dto;

import com.sparta.sparta_board.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentResponseDto {

    // 응답을 보낼 데이터 중 어떤 데이터를 보여줄지 선택해서 필드에 넣는다.
    // 생성자를 만들 때, comment.getUser.getUsername() 처럼 만들었던 comment entity에 있는 user와 board의 접근해서
    // 각각의 getter를 사용할 수 있다.
    private String comment;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long id;
    private String username;

    public CommentResponseDto(Comment comment) {
        this.comment = comment.getComment();
        this.createdAt =comment.getCreatedAt();
        this.modifiedAt =comment.getModifiedAt();
        this.id =comment.getId();
        this.username=comment.getUsername();

    }
}
