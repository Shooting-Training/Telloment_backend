package cau.capstone.backend.page.model.repository;


import cau.capstone.backend.page.model.Book;
import cau.capstone.backend.page.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    List<Book> findAllByUserId(Long userId); // 해당 유저의 모든 페이지 조회

    boolean existsByIdAndUserId(Long bookId, Long userId); // 해당 유저의 페이지가 존재하는지 확인

    List<Book> findByCategory(Category category);

    @Query("SELECT b FROM Book b LEFT JOIN b.hashtags h WHERE b.bookName LIKE %:name% OR h.tag IN :hashtags")
    org.springframework.data.domain.Page<Book> findByBookNameContainingOrHashtagsIn(@Param("name") String name, @Param("hashtags") Set<String> hashtags, Pageable pageable);

}
