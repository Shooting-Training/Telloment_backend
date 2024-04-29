package cau.capstone.backend.repository;

import cau.capstone.backend.model.entity.Moment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MomentRepository extends JpaRepository<Moment, Long> {
}
