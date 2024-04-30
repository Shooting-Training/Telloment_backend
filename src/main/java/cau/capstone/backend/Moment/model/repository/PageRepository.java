package cau.capstone.backend.Moment.model.repository;


import cau.capstone.backend.Moment.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
}
