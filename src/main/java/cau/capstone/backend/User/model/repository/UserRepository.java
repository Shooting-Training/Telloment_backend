package cau.capstone.backend.User.model.repository;

import cau.capstone.backend.User.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByNameContaining(String name); //유저 검색
    boolean existsByEmail(String email); //이메일로 유저 검색
    boolean existsByName(String name); //유저 이름 중복 확인

    Optional<User> findByEmail(String email); //이메일로 유저 검색
    Optional<User> findByNickname(String nickname); //닉네임으로 유저 검색


    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(u.name) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(u.nickname) LIKE LOWER(concat('%', :keyword, '%'))")
    org.springframework.data.domain.Page<User> findByKeywordWithPaging(@Param("keyword") String keyword, Pageable pageable);
}





