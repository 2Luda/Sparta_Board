package com.sparta.sparta_board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "users") // db 테이블과 일대일로 매핑
@NoArgsConstructor
public class User {
    // User 정보를 담을 Entity 객체 역할

    @Id
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    // 유저는 여러개의 게시판을 만들 수 있다.
    // 그러므로 OneToMany를 사용하고, Board를 List로 가져온다.
    @OneToMany(mappedBy = "user")
    List<Board> boards = new ArrayList<>();


    public User(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean isBoardWriter(Board board) {
        return !this.username.equals(board.getUser().getUsername()) && this.role == UserRoleEnum.USER;
    }

    public boolean isCommentWriter(Comment comment) {
        return !this.username.equals(comment.getUsername()) && this.role == UserRoleEnum.USER;
    }

}
