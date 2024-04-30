package cau.capstone.backend.Moment.model.repository;

import cau.capstone.backend.Moment.model.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long>{

    boolean existsByIdAndUserId(Long id, UUID userId); // 해당 유저의 스크랩인지 확인

    boolean existsByUserIdAndMomentId(Long userId, Long momentId); // 해당 유저가 해당 게시물을 스크랩했는지 확인

    List<Scrap> findAllByUserId(UUID userId); // 스크랩 목록 조회

}
