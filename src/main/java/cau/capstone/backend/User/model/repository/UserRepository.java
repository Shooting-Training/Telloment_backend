package cau.capstone.backend.User.model.repository;

import cau.capstone.backend.User.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByNameContaining(String name); //유저 검색
    boolean existsByEmail(String email); //이메일로 유저 검색
    boolean existsByName(String name); //유저 이름 중복 확인

    Optional<User> findByEmail(String email); //이메일로 유저 검색
    Optional<User> findByNickname(String nickname); //닉네임으로 유저 검색



}



