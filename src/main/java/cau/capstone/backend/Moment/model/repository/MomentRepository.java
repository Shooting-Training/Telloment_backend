package cau.capstone.backend.Moment.model.repository;

import cau.capstone.backend.Moment.model.Moment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;



@Repository
public interface MomentRepository extends JpaRepository<Moment, Long> {

    boolean existsByIdAndUserId(Long id, Long userId); // 해당 유저의 게시물인지 확인

    List<Moment> findAllByUserId(Long userId); // 해당 유저의 모든 게시물 조회

    List<Moment> findAllByUserIdAndCreatedAtAfter(Long userId, Long createdAt); // 해당 유저의 특정 시간 이후 게시물 조회

    List<Moment> findAllByUserIdAndCreatedAtBefore(Long userId, Long createdAt); // 해당 유저의 특정 시간 이전 게시물 조회

    List<Moment> findAllByUserIdAndCreatedAtBetween(Long userId, Long start, Long end); // 해당 유저의 특정 시간 사이 게시물 조회

    List<Moment> findAllByUserIdAndCreatedAt(Long userId, LocalDate createdAt); // 해당 유저의 특정 시간 게시물 조회

}
