package cau.capstone.backend.voice.service;

import cau.capstone.backend.User.model.repository.UserRepository;
import cau.capstone.backend.global.util.api.ResponseCode;
import cau.capstone.backend.global.util.exception.VoiceException;
import cau.capstone.backend.voice.dto.response.VoiceResponseDto;
import cau.capstone.backend.voice.model.VoiceScrapEntity;
import cau.capstone.backend.voice.model.VoiceScrapKey;
import cau.capstone.backend.voice.repository.VoiceRepository;
import cau.capstone.backend.voice.repository.VoiceScrapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoiceService {
    private final VoiceRepository voiceRepository;
    private final VoiceScrapRepository voiceScrapRepository;
    private final UserRepository userRepository;


    public Page<VoiceResponseDto> getAllByPage(Pageable pageable) {
        var list = voiceRepository.findAll(pageable).stream()
                .map(VoiceResponseDto::from)
                .collect(Collectors.toList());

        return new PageImpl<>(list);
    }

    @Transactional
    public void scrapVoiceByUser(String email, Long voiceId) {

        var user = userRepository.findByEmail(email).orElseThrow();
        Long userId = user.getId();

        var key = new VoiceScrapKey(userId, voiceId);
        voiceScrapRepository.findById(key)
                .ifPresent(entity -> {
                    throw new VoiceException(ResponseCode.VOICE_SCRAP_FAILURE);
                });

        voiceScrapRepository.save(
                VoiceScrapEntity.builder()
                        .id(key)
                        .build()
        );
    }

    @Transactional
    public void deleteScrapVoiceByUser(String email, Long voiceId) {
        Long userId = userRepository.findByEmail(email).orElseThrow().getId();
        var key = new VoiceScrapKey(userId, voiceId);
        voiceScrapRepository.findById(key)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found"));
        voiceScrapRepository.deleteById(key);
    }

    public List<VoiceResponseDto> getScrappedVoiceList(String email) {
        Long userId = userRepository.findByEmail(email).orElseThrow().getId();
        var list = voiceScrapRepository.findAllByIdUserId(userId); //todo; remove
        return voiceScrapRepository.findAllByIdUserIdAndUserVoiceUsePermissionFlag(userId, true).stream()
                .map(VoiceResponseDto::from)
                .collect(Collectors.toList());
    }
}
