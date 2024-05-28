package cau.capstone.backend.voice.service;

import cau.capstone.backend.User.model.User;
import cau.capstone.backend.User.model.repository.UserRepository;
import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.global.util.api.ResponseCode;
import cau.capstone.backend.global.util.exception.UserException;
import cau.capstone.backend.voice.dto.response.VoiceResponseDto;
import cau.capstone.backend.voice.model.VoiceScrapEntity;
import cau.capstone.backend.voice.model.VoiceScrapKey;
import cau.capstone.backend.voice.repository.VoiceRepository;
import cau.capstone.backend.voice.repository.VoiceScrapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final VoiceRepository voiceRepository;
    private final VoiceScrapRepository voiceScrapRepository;

    private final UserRepository userRepository;

    public List<VoiceResponseDto> getAccessableVoiceList() {
        return voiceRepository.findAllByProcessFlag(0).stream()
                .map(VoiceResponseDto::from)
                .collect(Collectors.toList());
    }

    public VoiceResponseDto getVoiceByToken(String accessToken) {
        String email = jwtTokenProvider.getUserEmail(accessToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
        Long userId = user.getId();
        var entity = voiceRepository.findById(userId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Voice not found"));
        return VoiceResponseDto.from(entity);
    }

    @Transactional
    public void scrapVoiceByUser(String accessToken, Long voiceId) {
        String email = jwtTokenProvider.getUserEmail(accessToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
        Long userId = user.getId();
        var key = new VoiceScrapKey(userId, voiceId);
        voiceScrapRepository.findById(key)
                .ifPresent(entity -> {
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Already scrapped");
                });

        voiceScrapRepository.save(
                VoiceScrapEntity.builder()
                        .id(key)
                        .build()
        );
    }

    @Transactional
    public void deleteScrapVoiceByUser(String accessToken, Long voiceId) {
        String email = jwtTokenProvider.getUserEmail(accessToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
        Long userId = user.getId();
        var key = new VoiceScrapKey(userId, voiceId);
        voiceScrapRepository.findById(key)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not found"));
        voiceScrapRepository.deleteById(key);
    }
}
