package cau.capstone.backend.Moment.model.repository;


import cau.capstone.backend.Moment.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {


    List<Page> findAllByUserId(Long userId); // 해당 유저의 모든 페이지 조회
}
