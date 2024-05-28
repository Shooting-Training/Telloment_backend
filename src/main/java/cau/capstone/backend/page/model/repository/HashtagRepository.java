package cau.capstone.backend.page.model.repository;

import cau.capstone.backend.page.model.Book;
import cau.capstone.backend.page.model.Hashtag;
import cau.capstone.backend.page.model.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByTag(String tag);

    @Query("SELECT p FROM Page p JOIN p.hashtags h WHERE h.tag = :tag")
    List<Page> findPagesByHashtag(@Param("tag") String tag);

    @Query("SELECT b FROM Book b JOIN b.hashtags h WHERE h.tag = :tag")
    List<Book> findBooksByHashtag(@Param("tag") String tag);

    @Query("SELECT p FROM Page p JOIN p.hashtags h WHERE h.tag = :tag")
    org.springframework.data.domain.Page<Page> findPagesByHashtag(@Param("tag") String tag, Pageable pageable);

}