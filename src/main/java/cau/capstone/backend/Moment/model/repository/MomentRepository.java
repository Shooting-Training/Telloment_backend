package cau.capstone.backend.Moment.model.repository;

import cau.capstone.backend.Moment.model.Moment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface MomentRepository extends JpaRepository<Moment, Long> {

    boolean existsByIdAndUserId(Long id, UUID userId); // 해당 유저의 게시물인지 확인

    List<Moment> findAllByUserId(UUID userId); // 해당 유저의 모든 게시물 조회

}
