package cau.capstone.backend.page.model.repository;


import cau.capstone.backend.page.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    List<Book> findAllByUserId(Long userId); // 해당 유저의 모든 페이지 조회
}
