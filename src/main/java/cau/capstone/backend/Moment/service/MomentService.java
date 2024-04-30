package cau.capstone.backend.Moment.service;

import cau.capstone.backend.Moment.dto.request.*;
import cau.capstone.backend.Moment.model.Like;
import cau.capstone.backend.Moment.model.Moment;
import cau.capstone.backend.Moment.model.Page;
import cau.capstone.backend.Moment.model.Scrap;
import cau.capstone.backend.Moment.model.repository.LikeRepository;
import cau.capstone.backend.Moment.model.repository.PageRepository;
import cau.capstone.backend.User.model.User;
import cau.capstone.backend.Moment.dto.response.ResponseScrapDto;
import cau.capstone.backend.User.model.repository.FollowRepository;
import cau.capstone.backend.Moment.model.repository.MomentRepository;
import cau.capstone.backend.Moment.model.repository.ScrapRepository;
import cau.capstone.backend.User.model.repository.UserRepository;
import cau.capstone.backend.global.util.api.ResponseCode;
import cau.capstone.backend.global.util.exception.LikeException;
import cau.capstone.backend.global.util.exception.MomentException;
import cau.capstone.backend.global.util.exception.ScrapException;
import cau.capstone.backend.global.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final LikeRepository likeRepository;
    private final PageRepository pageRepository;


    @Transactional
    public Moment readMoment(Long momentId){
        Moment moment = getMomentById(momentId);
        return moment;
    }

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
    public void deleteMoment(Long momentId, UUID userId, LocalDate date){
        validateMoment(momentId, userId);
        momentRepository.deleteById(momentId);
        log.info("moment deleted: " + momentId);
    }

    //모먼트 스크랩
    @Transactional
    public Long saveScrap(CreateScrapDto createScrapDto){
        Moment moment = getMomentById(createScrapDto.getMomentId());

        boolean isScrapped = scrapRepository.existsByUserIdAndMomentId(createScrapDto.getUserId(), createScrapDto.getMomentId());

        if (isScrapped){
            throw new ScrapException(ResponseCode.SCRAP_ALREADY_EXIST);
            return null;
        } else{
            //createScrap
            return scrapRepository.save(scrap).getId();
        }
    }

    //스크랩 리스트 반환
    @Transactional(readOnly = true)
    public List<ResponseScrapDto> getScrapList(UUID userId){
        validateUser(userId);
        List<Scrap> scrapList = scrapRepository.findAllByUserId(userId);
        log.info("scrap list returned: " + scrapList.size());
        return scrapList.stream().map(scrap -> ResponseScrapDto.builder()
                        .scrapId(scrap.getId())
                        .build())
                .collect(Collectors.toList());
    }

    //스크랩 해제
    @Transactional
    public void deleteScrap(Long scrapId, UUID userId){
        validateScrap(scrapId, userId);
        Scrap scrap = getScrapById(scrapId);

        //스크랩으로 모먼트를 찾아 모먼트의 정보도 변경
        Moment moment = momentRepository.findById(scrap.getMoment().getId())
                .orElseThrow(() -> new MomentException(ResponseCode.MOMENT_NOT_FOUND));
        moment.getScraps().remove(scrap);

        scrapRepository.deleteById(scrapId);
        momentRepository.save(moment);

        log.info("scrap deleted: " + scrapId);

    }

    //모먼트 연결하기
    @Transactional
    public void linkMoment(LinkMomentDto linkMomentDto){

        Long prevMomentId = linkMomentDto.getPrevMomentId();
        Long nextMomentId = linkMomentDto.getNextMomentId();

        Moment prevMoment = getMomentById(prevMomentId);
        Moment linkedMoment = getMomentById(nextMomentId);


        validateMoment(prevMoment.getId(), prevMoment.getUser().getId()) ;
        validateMoment(linkedMoment.getId(), linkedMoment.getUser().getId());

        //첫 모먼트 바로 다음에 연결
        if (prevMoment.getRootId() == 0){
            linkedMoment.setRootId(prevMoment);
            linkedMoment.setPrevId(prevMoment);
            prevMoment.setNextId(linkedMoment);

            momentRepository.save(prevMoment);
            momentRepository.save(linkedMoment);
        }

        //모먼트 연결 순서가 세 번째 이상
        else if(prevMoment.getRootId() != 0){
            Moment rootMoment = momentRepository.findById(prevMoment.getRootId())
                    .orElseThrow(() -> new MomentException(ResponseCode.MOMENT_NOT_FOUND));

            prevMoment.setNextId(linkedMoment);
            linkedMoment.setPrevId(prevMoment);
            linkedMoment.setRootId(rootMoment);

            momentRepository.save(prevMoment);
            momentRepository.save(linkedMoment);

        }
    }



    @Transactional
    public void likeMoment(Long momentId, UUID userId){
        Moment moment = getMomentById(momentId);
        User user = getUserById(userId);

        Like like = Like.createLike(user, moment);

        List<Like> likes = moment.getLikes();
        likes.add(like);
        moment.setLikes(likes);

        likeRepository.save(like);
        momentRepository.save(moment);
    }

    @Transactional
    public void dislikeMoment(Long momentId, UUID userId){
        Moment moment = getMomentById(momentId);
        User user = getUserById(userId);

        Like like = likeRepository.findByUserIdAndMomentId(userId, momentId);

        if(like == null){
            throw new LikeException(ResponseCode.LIKE_NOT_FOUND);
        }
        else{
            List<Like> likes = moment.getLikes();
            likes.remove(like);
            moment.setLikes(likes);

            likeRepository.delete(like);
            momentRepository.save(moment);
        }
    }

    @Transactional
    public List<Moment> getUserMomentList(UUID userId){
        return momentRepository.findAllByUserId(userId);
    }

    @Transactional
    public List<Like> getUserLikeList(UUID userId){
        return likeRepository.findAllByUserId(userId);
    }

    @Transactional
    public List<Scrap> getUserScrapList(UUID userId){
        return scrapRepository.findAllByUserId(userId);
    }

    @Transactional
    public List<Page> getUserPageList(UUID userId){
        return pageRepository.findAllByUserId(userId);
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



    private User getUserById(UUID userId){
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


    private void validateUser(UUID userId){
        if(!userRepository.existsById(userId)){
            throw new UserException(ResponseCode.USER_NOT_FOUND);
        }
    }

    private void validateMoment(Long momentId, UUID userId){
        if(!momentRepository.existsById(momentId)){
            throw new MomentException(ResponseCode.MOMENT_NOT_FOUND);
        }
        if (!momentRepository.existsByIdAndUserId(momentId, userId)){ //모먼트의 주인이 아닌지 확인
            throw new MomentException(ResponseCode.MOMENT_NOT_OWNED);
        }
    }

    private void validateScrap(Long scrapId, UUID userId){
        if(!scrapRepository.existsById(scrapId)){
            throw new ScrapException(ResponseCode.SCRAP_NOT_FOUND);
        }
        if(!scrapRepository.existsByIdAndUserId(scrapId, userId)){ //유저가 스크랩 한 게 맞는지 확인
            throw new ScrapException(ResponseCode.SCARP_NOT_OWNED);
        }
    }
}
