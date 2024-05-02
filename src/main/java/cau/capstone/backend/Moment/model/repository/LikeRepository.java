package cau.capstone.backend.Moment.model.repository;

import cau.capstone.backend.Moment.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository

public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findByUserIdAndMomentId(Long userId, Long momentId); // 해당 유저가 해당 게시물을 좋아요 했는지 확인

    List<Like> findAllByUserId(Long userId); // 해당 유저가 좋아요한 모든 게시물 조회
}
