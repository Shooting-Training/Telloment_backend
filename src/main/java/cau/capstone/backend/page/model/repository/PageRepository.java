package cau.capstone.backend.page.model.repository;

import cau.capstone.backend.page.model.EmotionType;
import cau.capstone.backend.page.model.Page;

import org.springframework.data.domain.Pageable;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    boolean existsByIdAndUserId(Long id, Long userId); // 해당 유저의 게시물인지 확인

    List<Page> findAllByUserId(Long userId); // 해당 유저의 모든 게시물 조회

    List<Page> findAllByUserIdAndCreatedAt(Long userId, LocalDate createdAt); // 해당 유저의 특정 시간 게시물 조회

    boolean existsByIdAndBookId(Long id, Long bookId); // 해당 북의 게시물인지 확인

    Optional<Page> findPageById(Long pageId); // 해당 게시물 조회

    // JPQL을 사용하여 title 또는 content에 키워드가 포함되거나, 해당 페이지의 해시태그 중 하나가 키워드를 포함하는 Page 검색
    @Query("SELECT p FROM Page p LEFT JOIN p.hashtags h WHERE LOWER(p.title) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(p.content) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(h.tag) LIKE LOWER(concat('%', :keyword, '%'))")
    List<Page> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT p FROM Page p LEFT JOIN p.hashtags h WHERE LOWER(p.title) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(p.content) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(h.tag) LIKE LOWER(concat('%', :keyword, '%')) GROUP BY p")
    org.springframework.data.domain.Page<Page> findByKeywordWithPaging(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Page p WHERE p.emotion.type = :emotionType")
    org.springframework.data.domain.Page<Page> findByEmotionType(EmotionType emotionType, Pageable pageable);

    @Query("SELECT p FROM Page p WHERE p.createdAt >= :startTime")
    org.springframework.data.domain.Page<Page> findAllCreatedWithinLast24Hours(@Param("startTime") LocalDateTime startTime, Pageable pageable);


    @Query("SELECT p FROM Page p WHERE p.createdAt >= :startTime AND p.emotion.type = :emotionType")
    org.springframework.data.domain.Page<Page> findAllByEmotionTypeCreatedWithinLast24Hours(@Param("startTime") LocalDateTime startTime, @Param("emotionType") EmotionType emotionType, Pageable pageable);

    @Query("SELECT p FROM Page p JOIN p.hashtags h WHERE p.createdAt >= :startTime AND LOWER(h.tag) LIKE LOWER(concat('%', :hashTag, '%')) GROUP BY p")
    org.springframework.data.domain.Page<Page> findAllByHashTagCreatedWithinLast24Hours(@Param("startTime") LocalDateTime startTime, @Param("hashTag") String hashTag, Pageable pageable);


    @Query("SELECT p FROM Page p JOIN p.hashtags h WHERE p.createdAt >= :startTime AND p.emotion.type = :emotionType AND LOWER(h.tag) LIKE LOWER(concat('%', :hashTag, '%')) GROUP BY p")
    org.springframework.data.domain.Page<Page> findAllByEmotionTypeAndHashTagCreatedWithinLast24Hours(@Param("startTime") LocalDateTime startTime, @Param("emotionType") EmotionType emotionType, @Param("hashTag") String hashTag, Pageable pageable);

}
