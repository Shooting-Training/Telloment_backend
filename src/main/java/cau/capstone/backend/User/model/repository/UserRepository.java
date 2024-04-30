package cau.capstone.backend.User.model.repository;

import cau.capstone.backend.User.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> finalAllByNameContaining(String name); //유저 검색
    boolean existsByName(String name); //유저 이름 중복 확인
    boolean existsByKeyCode(String keyCode); //키코드 중복 확인

    Optional<User> findByKeyCode(String keyCode); //키코드로 유저 찾기



}



