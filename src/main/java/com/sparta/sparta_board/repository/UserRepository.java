package com.sparta.sparta_board.repository;

import com.sparta.sparta_board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 유저 정보를 DB에 저장하거나 찾는 역할
    Optional<User> findByUsername(String username);
}
