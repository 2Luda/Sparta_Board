package com.sparta.sparta_board.repository;

import com.sparta.sparta_board.dto.CommentResponseDto;
import com.sparta.sparta_board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByOrderByModifiedAtDesc();
}
