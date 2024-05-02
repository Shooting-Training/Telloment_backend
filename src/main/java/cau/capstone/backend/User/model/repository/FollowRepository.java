package cau.capstone.backend.User.model.repository;


import cau.capstone.backend.User.model.Follow;
import cau.capstone.backend.User.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FollowRepository extends JpaRepository<Follow, Long> {
//    @Query(value = "select u from Follow f INNER JOIN User u ON f.followee = u.id where f.follower = :userId") // 팔로우 목록 조회
//    List<User> findAllByFromUser(@Param("userId") Long userId);

    boolean existsByFolloweeIdAndFollowerId(Long followee, Long follower); // 팔로우 여부 확인
    void deleteByFolloweeIdAndFollowerId(Long followee, Long follower); // 팔로우 취소
}