//package cau.capstone.backend.page.model.repository;
//
//import cau.capstone.backend.page.model.Scrap;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//
//
//@Repository
//public interface ScrapRepository extends JpaRepository<Scrap, Long>{
//
//    boolean existsByIdAndUserId(Long id, Long userId); // 해당 유저의 스크랩인지 확인
//
//    boolean existsByUserIdAndPageId(Long userId, Long pageId); // 해당 유저가 해당 게시물을 스크랩했는지 확인
//
//    List<Scrap> findAllByUserId(Long userId); // 스크랩 목록 조회
//
//}
