package cau.capstone.backend.page.model.repository;

import cau.capstone.backend.page.model.Like;
import cau.capstone.backend.page.model.LikeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository

public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findByLikeTypeAndTargetId(LikeType likeType, Long targetId);

    Optional<Like> findByLikeTypeAndTargetIdAndUserId(LikeType likeType, Long targetId, Long userId);

    boolean existsByLikeTypeAndTargetIdAndUserId(LikeType likeType, Long targetId, Long userId);

    int countByLikeTypeAndTargetId(LikeType likeType, Long targetId);

    void deleteAllByLikeTypeAndTargetId(LikeType likeType, Long targetId);
}
