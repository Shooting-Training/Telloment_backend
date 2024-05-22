package cau.capstone.backend.page.model.repository;

import cau.capstone.backend.page.model.Page;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    boolean existsByIdAndUserId(Long id, Long userId); // 해당 유저의 게시물인지 확인

    List<Page> findAllByUserId(Long userId); // 해당 유저의 모든 게시물 조회

    List<Page> findAllByUserIdAndCreatedAt(Long userId, LocalDate createdAt); // 해당 유저의 특정 시간 게시물 조회

    boolean existsByIdAndBookId(Long id, Long bookId); // 해당 북의 게시물인지 확인

    Optional<Page> findPageById(Long pageId); // 해당 게시물 조회

}
