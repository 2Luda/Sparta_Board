package com.sparta.sparta_board.repository;

import com.sparta.sparta_board.dto.BoardListResponseDto;
import com.sparta.sparta_board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface BoardRepository extends JpaRepository<Board,Long> {

    // 저장된 Board 데이터가 모디는 곳
    // @Repository 어노테이션 붙이기
    // Class 를 interface 키워드로 바꾸기
    // JpaRepository 상속받고, 사용할 Entity는 Board 로 타입은 Long으로
    // BoardListResponseDto가 쓸 시간 내림차순 정렬 기능 추가 (List<BoardListResponseDto> findAllByOrderByModifiedAtDesc();)
    List<Board> findAllByOrderByModifiedAtDesc();
}
