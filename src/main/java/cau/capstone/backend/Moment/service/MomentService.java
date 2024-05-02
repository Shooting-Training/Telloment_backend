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
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public long createMoment(CreateMomentDto createMomentDto) {
        User user = getUserById(createMomentDto.getUserId());
        Page page = pageRepository.findById(createMomentDto.getPageId())
                .orElseThrow(() -> new MomentException(ResponseCode.PAGE_NOT_FOUND));
        Moment moment = Moment.createMoment(user, createMomentDto.getTitle(), createMomentDto.getContent(), page);
        log.info("new moment saved: " + moment.getTitle());
        return momentRepository.save(moment).getId();
    }

    //모먼트 정보 수정
    @Transactional
    public void updateMoment(UpdateMomentDto updateMomentDto, LocalDateTime date){
        Moment moment = getMomentById(updateMomentDto.getMomentId());
        moment.updateMoment(updateMomentDto.getTitle(), updateMomentDto.getContent(), date);
        log.info("moment updated: " + moment.getTitle());
        momentRepository.save(moment);
    }

    //모먼트 삭제
    @Transactional
    public void deleteMoment(Long momentId, Long userId, LocalDateTime date){
        validateMoment(momentId, userId);
        momentRepository.deleteById(momentId);
        log.info("moment deleted: " + momentId);
    }
    @Transactional
    public void deleteMoment(Long momentId, Long userId){
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
        } else{
            User user = getUserById(createScrapDto.getUserId());
            //createScrap
            Scrap scrap = Scrap.createScrap(user , moment);
            return scrapRepository.save(scrap).getId();
        }
    }

    @Transactional
    public List<Moment> getMomentListByDate(Long userId, LocalDate date){
        List<Moment> momentList = momentRepository.findAllByUserIdAndCreatedAt(userId, date);
        log.info("moment list returned: " + momentList.size());
        return momentList;
    }

    //스크랩 리스트 반환
    @Transactional(readOnly = true)
    public List<ResponseScrapDto> getScrapList(Long userId){
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
    public void deleteScrap(Long scrapId, Long userId){
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
        if (prevMoment.getRootId() == -1){
            linkedMoment.setRootId(prevMoment);
            linkedMoment.setPrevId(prevMoment);
            prevMoment.setNextId(linkedMoment);

            momentRepository.save(prevMoment);
            momentRepository.save(linkedMoment);
        }

        //모먼트 연결 순서가 세 번째 이상
        else if(prevMoment.getRootId() != -1){
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
    public void likeMoment(Long momentId, Long userId){
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
    public void dislikeMoment(Long momentId, Long userId){
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
    public Long addMomentToPage(AddMomentToPageDto addMomentToPageDto) {
        Moment moment = getMomentById(addMomentToPageDto.getMomentId());
        Page page = pageRepository.findById(addMomentToPageDto.getPageId())
                .orElseThrow(() -> new MomentException(ResponseCode.PAGE_NOT_FOUND));

        moment.setPage(page);
        page.addMoment(moment);

        pageRepository.save(page);
        return momentRepository.save(moment).getId();
    }


    @Transactional
    public void deleteMomentFromPage(DeleteMomentFromPageDto deleteMomentFromPageDto){
        Moment moment = getMomentById(deleteMomentFromPageDto.getMomentId());
        Page page = pageRepository.findById(deleteMomentFromPageDto.getPageId())
                .orElseThrow(() -> new MomentException(ResponseCode.PAGE_NOT_FOUND));

        moment.setPage(null);
        page.getMoments().remove(moment);

        pageRepository.save(page);
        momentRepository.save(moment);
    }


    @Transactional
    public Long createMomentFromScrap(CreateMomentFromScrapDto createMomentFromScrapDto){
        validateScrap(createMomentFromScrapDto.getScrapId(), createMomentFromScrapDto.getUserId());


        Scrap scrap = getScrapById(createMomentFromScrapDto.getScrapId());
        User user = getUserById(createMomentFromScrapDto.getUserId());
        Page page = pageRepository.findById(createMomentFromScrapDto.getPageId())
                .orElseThrow(() -> new MomentException(ResponseCode.PAGE_NOT_FOUND));

        Moment moment = scrap.createMomentFromScrap(user, scrap, page);

        return momentRepository.save(moment).getId();
    }

    @Transactional
    public List<Moment> getUserMomentList(Long userId){
        return momentRepository.findAllByUserId(userId);
    }

    @Transactional
    public List<Like> getUserLikeList(Long userId){
        return likeRepository.findAllByUserId(userId);
    }

    @Transactional
    public List<Scrap> getUserScrapList(Long userId){
        return scrapRepository.findAllByUserId(userId);
    }

    @Transactional
    public List<Page> getUserPageList(Long userId){
        return pageRepository.findAllByUserId(userId);
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
        if (!momentRepository.existsByIdAndUserId(momentId, userId)){ //모먼트의 주인이 아닌지 확인
            throw new MomentException(ResponseCode.MOMENT_NOT_OWNED);
        }
    }

    private void validateScrap(Long scrapId, Long userId){
        if(!scrapRepository.existsById(scrapId)){
            throw new ScrapException(ResponseCode.SCRAP_NOT_FOUND);
        }
        if(!scrapRepository.existsByIdAndUserId(scrapId, userId)){ //유저가 스크랩 한 게 맞는지 확인
            throw new ScrapException(ResponseCode.SCARP_NOT_OWNED);
        }
    }
}
