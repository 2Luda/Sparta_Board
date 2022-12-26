package com.sparta.sparta_board.entity;


import com.sparta.sparta_board.dto.BoardRequestDto;
import com.sparta.sparta_board.dto.LoginRequestDto;
import com.sparta.sparta_board.repository.UserRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board extends Timestamped{

    // 글 고유 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;


    // requestDto 정보를 가져와서 entity 만들 때 사용한다.
//    public Board(BoardRequestDto requestDto) {
//        this.title = requestDto.getTitle();
//        this.content = requestDto.getContent();
//    }

    public Board(BoardRequestDto requestDto, User user) {
        this.title= requestDto.getTitle();
        this.user= user;
        this.content = requestDto.getContent();
    }


    // 업데이트 메소드
//    public void update(BoardRequestDto requestDto) {
//        this.title = requestDto.getTitle();
//        this.content = requestDto.getContent();
//    }

//    public boolean checkPassword(String password) {
//        if(this.password.compareTo(password) == 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    //  Board - Comment: Board 하나는 여러 개의 게시글을 가질 수 있으므로 OneToMany 아다.
    @OneToMany(mappedBy = "board")
    @OrderBy("modifiedAt desc")
    private List<Comment> comments = new ArrayList<>();


    public void update(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void putCommentOnBoard(Comment comment) {
        this.comments.add(comment);
    }
}
