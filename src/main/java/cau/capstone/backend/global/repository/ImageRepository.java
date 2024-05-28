package cau.capstone.backend.global.repository;

import cau.capstone.backend.aws.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ImageRepository extends JpaRepository<Image, Long> {


}
