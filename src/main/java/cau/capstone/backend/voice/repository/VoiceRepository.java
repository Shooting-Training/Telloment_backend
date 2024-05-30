package cau.capstone.backend.voice.repository;

import cau.capstone.backend.voice.model.VoiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoiceRepository extends JpaRepository<VoiceEntity, Long> {
    Page<VoiceEntity> findAll(Pageable pageable);
}
