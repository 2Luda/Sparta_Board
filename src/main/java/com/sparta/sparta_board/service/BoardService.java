package com.sparta.sparta_board.service;

import com.sparta.sparta_board.dto.BoardListResponseDto;
import com.sparta.sparta_board.dto.BoardRequestDto;
import com.sparta.sparta_board.dto.BoardResponseDto;
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    // board 관련 비즈니스 로직 처리 코드들이 모이는 곳
    // 1. annotation 붙이기
    // 2. BoardRepository 가져오기
    // 3. 기능 메소드 만들기

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 게시글 생성
    // 1. requestDto 를 매개변수로 받는다
    // 2. 받은 매개변수를 이용해 새 Board 객체를 생성한다. (Board board = new Board(requestDto))
    // 3. boardRepository 에 생성한 객체를 저장한다. (boardRepository.save(board))
    // 4. 만든 board 객체를 이용해서, responseDto 객체도 새로 생성해서 리턴해준다.
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, Claims claims) {
            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 토큰값이 유효한 회원만 게시글 작성 가능
            Board board = new Board(requestDto,user);
            boardRepository.save(board);
            return new BoardResponseDto(board);
    }

    // 게시글 전체 조회
    // 1. BoardResponseDto의 list로 리턴타입 지정
    // 2. repository에서 findAllByOrderByModifiedAtDesc() 로 찾은 것을 리스트에 넣기
    // 3. 값을 담을 새 배열 만들기 (responseDTO 이용)
    // 4. foreach 문으로 board의 값 하나씩 ResponseDto를 이용해 새 객체 만들기
    // 5. Try catch 문으로 예외처리
    @Transactional(readOnly = true)
    public List<BoardListResponseDto> findAllBoard() {
        try {
            List<Board> boardList = boardRepository.findAllByOrderByModifiedAtDesc();
            List<BoardListResponseDto> responseDtoList = new ArrayList<>();

            for(Board board : boardList) {
                responseDtoList.add(new BoardListResponseDto(board));
            }

            return responseDtoList;
        } catch (Exception e) {
            throw new IllegalArgumentException("에러");
        }
    }

    @Transactional
    // 게시글 선택 조회
    //  1.ResponseDto 로 리턴 타입 설정
    //  2. 매개변수로 id값 받기
    //  3. repository에서 id를 기준으로 찾고, ElseThrow로 예외 처리
    //  4. 찾은 갑을 이용해 새로운 ResponseDto 객체로 리턴

    public BoardResponseDto findOneBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("조회 실패")
        );
        return new BoardResponseDto(board);

    }
    // 글 수정하기
    // 1. BoardResponseDto 으로 리턴 타입 설정
    // 2. 글을 수정해야 하니까 매개변수로 id와 BoardRequestDto requestDto, claims 를 받는다.
    // 3. Repository 에서 id를 기준으로 찾고, ElseThrow로 예외 처리
    // 4. 찾은 값을 board에 매개변수 requestDto를 넣는다.
    @Transactional
    // 게시글 수정
    public BoardResponseDto update(Long id, BoardRequestDto requestDto, Claims claims) {
        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );
        // boardRepository 에서 id가 있는지 검사, 없으면 익셉션 출력
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 id의 게시글이 존재하지 않습니다.")
        );
        // 해당 유저가 작성한 포스트가 맞는지 검사 > 맞으면 수정
        if (user.isBoardWriter(board)) {
            throw new UserException(UserExceptionType.ALREADY_TOKEN_NOT_POST_COMMENT);
        }

        // BoardResponseDto 생성 후 리턴
        board.update(requestDto);
        return new BoardResponseDto(board);

    }
    @Transactional
    // 글 삭제하기
    // 1. void 타입으로 리턴 타입 설정
    // 2. 글을 삭제해야하니 매개변수로 id를 받는다.
    // 3. deleteById() 기능을 이용하고 id를 리턴한다.
    public void delete(Long id, Claims claims) {
        // boardRepository 에서 id가 있는지 검사, 없으면 익셉션 출력
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 id의 게시글이 존재하지 않습니다.")
        );
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            if (user.isBoardWriter(board)) {
                throw new UserException(UserExceptionType.ALREADY_TOKEN_NOT_POST_COMMENT);
            }
        boardRepository.deleteById(id);
    }
}
