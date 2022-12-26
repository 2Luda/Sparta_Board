package com.sparta.sparta_board.service;

import com.sparta.sparta_board.dto.CommentRequestDto;
import com.sparta.sparta_board.dto.CommentResponseDto;
import com.sparta.sparta_board.entity.Board;
import com.sparta.sparta_board.entity.Comment;
import com.sparta.sparta_board.entity.User;
import com.sparta.sparta_board.exception.UserException;
import com.sparta.sparta_board.exception.UserExceptionType;
import com.sparta.sparta_board.jwt.JwtUtil;
import com.sparta.sparta_board.repository.BoardRepository;
import com.sparta.sparta_board.repository.CommentRepository;
import com.sparta.sparta_board.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;


    // 댓글 작성 서비스
    // 토큰을 검사하여 유효한 토큰일 경우 댓글 작성 가능
    // 산텍한 게시글의 DB 저장 유무를 확인하기
    // 선택한 게시글이 있다면 댓글을 등록하면 등록된 댓글 반환하기
    public CommentResponseDto createComment(CommentRequestDto requestDto, Long id, Claims claims) {

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("조회 실패")
        );
        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다")
        );
        Comment comment = new Comment(requestDto,user,board);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    // 댓글 수정 기능
    public CommentResponseDto update(Long id, CommentRequestDto requestDto, Claims claims) {
        // commentRepository에서 id를 찾는다. id가 없으면 익셉션 출력
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("조회 실패")
        );
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다")
            );
            if (user.isCommentWriter(comment)) {
                throw new UserException(UserExceptionType.ALREADY_TOKEN_NOT_POST_COMMENT);
            }

        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }


    // 댓글 삭제
    public void delete(Long id, Claims claims) {
        // commentRepository에서 id를 찾는다. id가 없으면 익셉션 출력
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("조회 실패")
        );
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            if(user.isCommentWriter(comment)) {
                throw new UserException(UserExceptionType.ALREADY_TOKEN_NOT_POST_COMMENT);
            }
        commentRepository.deleteById(id);
    }
}
