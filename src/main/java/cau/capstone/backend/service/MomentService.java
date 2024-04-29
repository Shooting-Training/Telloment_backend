package cau.capstone.backend.service;

import cau.capstone.backend.model.entity.Moment;
import cau.capstone.backend.model.entity.Scrap;
import cau.capstone.backend.model.entity.User;
import cau.capstone.backend.model.request.*;
import cau.capstone.backend.model.response.ResponseScrapDto;
import cau.capstone.backend.repository.FollowRepository;
import cau.capstone.backend.repository.MomentRepository;
import cau.capstone.backend.repository.ScrapRepository;
import cau.capstone.backend.repository.UserRepository;
import cau.capstone.backend.util.api.ResponseCode;
import cau.capstone.backend.util.exception.MomentException;
import cau.capstone.backend.util.exception.ScrapException;
import cau.capstone.backend.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MomentService {

    private final MomentRepository momentRepository; //
    private final ScrapRepository scrapRepository;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public long saveMoment(CreateMomentDto createMomentDto) {
        User user = getUserById(createMomentDto.getUserId());
        Moment moment = Moment.createMoment(createMomentDto.getTitle(), createMomentDto.getContent(), user, null, createMomentDto.getDate(), createMomentDto.getRootId(), createMomentDto.getPrevId(), createMomentDto.getNextId());
        log.info("new moment saved: " + moment.getTitle());
        return momentRepository.save(moment).getId();
    }

    //모먼트 정보 수정
    @Transactional
    public void updateMoment(UpdateMomentDto updateMomentDto, LocalDate date){
        Moment moment = getMomentById(updateMomentDto.getMomentId());
        moment.updateMoment(updateMomentDto.getTitle(), updateMomentDto.getContent(), date);
        log.info("moment updated: " + moment.getTitle());
        momentRepository.save(moment);
    }

    //모먼트 삭제
    @Transactional
    public void deleteMoment(Long momentId, Long userId, LocalDate date){
        validateMoment(momentId, userId);
        momentRepository.deleteById(momentId);
        log.info("moment deleted: " + momentId);
    }

    //모먼트 스크랩
    @Transactional
    public Long saveScrap(CreateScrapDto createScrapDto){
        validateMoment(createScrapDto.getMomentId(), createScrapDto.getUserId());
        User user = getUserById(createScrapDto.getUserId());
        Moment moment = getMomentById(createScrapDto.getMomentId());

        if (moment.isScrapped()){ //이미 스크랩 된 경우 x
            log.info(moment.getId() + " is already scrapped");
            throw new ScrapException(ResponseCode.SCRAP_ALREADY_SCRAPPED);
        }

        Scrap scrap = Scrap.createScrap(createScrapDto.getTitle(), createScrapDto.getContent(), user, moment);
        moment.setScrap(scrap);
        log.info("new scrap saved: " + scrap.getName());
        return scrapRepository.save(scrap).getId();
    }

    //스크랩 리스트 반환
    @Transactional(readOnly = true)
    public List<ResponseScrapDto> getScrapList(Long userId){
        validateUser(userId);
        List<Scrap> scrapList = scrapRepository.findALLByUserId(userId);
        log.info("scrap list returned: " + scrapList.size());
        return scrapList.stream().map(scrap -> ResponseScrapDto.builder()
                .scrapId(scrap.getId())
                .count(scrap.getCount())
                .build())
                .collect(Collectors.toList());
    }

    //스크랩 수정
    @Transactional
    public void updateScrap(UpdateScrapDto updateScrapDto){
        Scrap scrap = getScrapById(updateScrapDto.getScrapId());
        scrap.updateScrap(updateScrapDto.getName());
        log.info("scrap updated: " + scrap.getName());
        scrapRepository.save(scrap);
    }

    //스크랩 해제
    @Transactional
    public void deleteScrap(Long scrapId, Long userId){
        validateScrap(scrapId, userId);
        Scrap scrap = getScrapById(scrapId);
        scrap.getMoments().forEach(moment -> moment.setScrap(null)); // 스크랩된 모먼트들 스크랩 해제 null로 초기화
        scrapRepository.deleteById(scrapId);
        log.info("scrap deleted: " + scrapId);

    }

    @Transactional
    public Long createMomentFromScrap(CreateMomentFromScrapDto createMomentFromScrapDto){
        validateScrap(createMomentFromScrapDto.getScrapId(), createMomentFromScrapDto.getUserId());
        Scrap scrap = getScrapById(createMomentFromScrapDto.getScrapId());
        Moment moment = scrap.createMomentFromScrap(createMomentFromScrapDto.getTitle(), createMomentFromScrapDto.getContent(),
                createMomentFromScrapDto.getDate(), createMomentFromScrapDto.getRootId(), createMomentFromScrapDto.getPrevId(), createMomentFromScrapDto.getNextId());
        moment.setScrap(scrap);
        return momentRepository.save(moment).getId();
    }

    private User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }

    private Moment getMomentById(Long momentId){
        return momentRepository.findById(momentId)
                .orElseThrow(() -> new MomentException(ResponseCode.MOMENT_NOT_FOUND));
    }

    private Scrap getScrapById(Long scrapId){
        return scrapRepository.findById(scrapId)
                .orElseThrow(() -> new ScrapException(ResponseCode.SCRAP_NOT_FOUND));
    }


    private void validateUser(Long userId){
        if(!userRepository.existsById(userId)){
            throw new UserException(ResponseCode.USER_NOT_FOUND);
        }
    }

    private void validateMoment(Long momentId, Long userId){
        if(!momentRepository.existsById(momentId)){
            throw new MomentException(ResponseCode.MOMENT_NOT_FOUND);
        }
        if (!scrapRepository.existsByIdAndUserId(momentId, userId)){ //모먼트의 주인이 아닌지 확인
            throw new MomentException(ResponseCode.MOMENT_NOT_OWNED);
        }
    }

    private void validateScrap(Long scrapId, Long userId){
        if(!scrapRepository.existsById(scrapId)){
            throw new ScrapException(ResponseCode.SCRAP_NOT_FOUND);
        }
        if(!scrapRepository.existsByIdAndUserId(scrapId, userId)){ //유저가 스크랩 한 게 맞는지 확인
            throw new ScrapException(ResponseCode.SCRAP_NOT_OWNED);
        }
    }
}
