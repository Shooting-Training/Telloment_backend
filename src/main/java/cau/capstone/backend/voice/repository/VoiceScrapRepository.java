package cau.capstone.backend.voice.repository;

import cau.capstone.backend.voice.model.VoiceScrapEntity;
import cau.capstone.backend.voice.model.VoiceScrapKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoiceScrapRepository extends JpaRepository<VoiceScrapEntity, VoiceScrapKey> {
    List<VoiceScrapEntity> findAllByIdUserId(Long userId);

    List<VoiceScrapEntity> findAllByIdVoiceId(Long voiceId);

}
