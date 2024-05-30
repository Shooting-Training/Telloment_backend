package cau.capstone.backend.page.service;


import cau.capstone.backend.page.model.Category;
import cau.capstone.backend.User.service.ScoreService;
import cau.capstone.backend.global.redis.RankingService;
import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.page.dto.request.*;
import cau.capstone.backend.page.dto.response.ResponsePageDto;
import cau.capstone.backend.page.model.*;
import cau.capstone.backend.page.model.repository.*;
import cau.capstone.backend.User.model.User;
import cau.capstone.backend.User.model.repository.UserRepository;
import cau.capstone.backend.global.util.api.ResponseCode;
import cau.capstone.backend.global.util.exception.LikeException;
import cau.capstone.backend.global.util.exception.PageException;
import cau.capstone.backend.global.util.exception.ScrapException;
import cau.capstone.backend.global.util.exception.UserException;
import cau.capstone.backend.voice.dto.response.VoiceResponseDto;
import cau.capstone.backend.voice.repository.VoiceRepository;
import com.amazonaws.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PageService {

    private final PageRepository pageRepository; //
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final BookRepository bookRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final ScoreService scoreService;
    private final LikeService likeService;
    private final RankingService rankingService;
    private final HashtagRepository hashtagRepository;


    @Transactional
    public ResponsePageDto getPage(String accessToken, Long pageId) {

        String userEmail = jwtTokenProvider.getUserEmail(accessToken);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
        Long userId = user.getId();

        Page page = getPageById(pageId);
        Book book = getBookById(page.getBook().getId());
        page.setViewCount(page.getViewCount() + 1);
        book.setBookViewCount(book.getBookViewCount() + 1);
        pageRepository.save(page);
        bookRepository.save(book);

        rankingService.incrementViewCountPage(page.getId(), page.getEmotion().getType());
        rankingService.incrementViewCountBook(page.getBook().getId(), page.getBook().getCategory());
        scoreService.plusViewScore(userId, page);

        return ResponsePageDto.from(page);
}

    public org.springframework.data.domain.Page<ResponsePageDto> searchPagesWithKeywordAndPaging(String keyword, Pageable pageable) {
        Pageable sortedByCreationDateDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        org.springframework.data.domain.Page<Page> pages = pageRepository.findByKeywordWithPaging(keyword, sortedByCreationDateDesc);

        List<ResponsePageDto> responsePageDtoList = new ArrayList<>();

        for (Page page : pages) {
            ResponsePageDto responsePageDto = ResponsePageDto.from(page);
            responsePageDto.setLikeCount(likeService.countLikesForPage(page.getId()));
            responsePageDtoList.add(responsePageDto);
        }

        return new PageImpl<>(responsePageDtoList, sortedByCreationDateDesc, pages.getTotalElements());
    }


    public org.springframework.data.domain.Page<ResponsePageDto> findAllPages(Pageable pageable) {

        Pageable sortedByCreationDateDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        org.springframework.data.domain.Page<Page> pages = pageRepository.findAll(sortedByCreationDateDesc);

        List<ResponsePageDto> responsePageDtoList = new ArrayList<>();

        for (Page page : pages) {
            ResponsePageDto responsePageDto = ResponsePageDto.from(page);
            responsePageDto.setLikeCount(likeService.countLikesForPage(page.getId()));
            responsePageDtoList.add(responsePageDto);
        }


        return new PageImpl<>(responsePageDtoList, sortedByCreationDateDesc, pages.getTotalElements());
    }

    public org.springframework.data.domain.Page<ResponsePageDto> findPagesByEmotionType(EmotionType emotionType, Pageable pageable) {
        Pageable sortedByCreationDateDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        org.springframework.data.domain.Page<Page> pages = pageRepository.findByEmotionType(emotionType, sortedByCreationDateDesc);

        List<ResponsePageDto> responsePageDtoList = new ArrayList<>();

        for (Page page : pages) {
            ResponsePageDto responsePageDto = ResponsePageDto.from(page);
            responsePageDto.setLikeCount(likeService.countLikesForPage(page.getId()));
            responsePageDtoList.add(responsePageDto);
        }

        return new PageImpl<>(responsePageDtoList, sortedByCreationDateDesc, pages.getTotalElements());
    }

    @Transactional
    public ResponsePageDto createPage(CreatePageDto createPageDto, String accessToken) {
        String userEmail = jwtTokenProvider.getUserEmail(accessToken);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));

        Book book = getBookById(createPageDto.getBookId());

        Page page = Page.createPage(user, book, createPageDto.getTitle(), createPageDto.getContent());

        setHashtagsToPage(page, createPageDto.getHashtags());
        setPageEmotion(page, createPageDto.getEmotionType(), createPageDto.getEmotionIntensity());

        pageRepository.save(page);

        ResponsePageDto responsePageDto = ResponsePageDto.from(page);

        return responsePageDto;
    }

    //먼저 페이지를 생성하고, 페이지의 감정을 다시 반환받아 설정한다.
    @Transactional
    public void setPageEmotion(Page page, String emotionCode, int emotionIntensity){

        if(page == null){
            throw new PageException(ResponseCode.PAGE_NOT_FOUND);
        }

        System.out.println("emotionCode: " + emotionCode);
        System.out.println("emotionIntensity: " + emotionIntensity);

        if(emotionCode == null || EmotionType.getByCode(emotionCode) == null || emotionCode.isEmpty() ){
            emotionCode = "NEUTRAL";
        }

        if(emotionIntensity <0 || emotionIntensity > 3){
            emotionIntensity = 0;
        }

        System.out.println("emotionCode: " + emotionCode);
        System.out.println("emotionIntensity: " + emotionIntensity);

        page.setEmotion(emotionCode, emotionIntensity);


//        return ResponsePageDto.from(page);
    }

    @Transactional
    public List<ResponsePageDto> getPageList(Long userId){
        validateUser(userId);
        List<Page> pageList = pageRepository.findAllByUserId(userId);

        List<ResponsePageDto> responsePageDtoList = new ArrayList<>();

        for(Page page : pageList){
            ResponsePageDto responsePageDto = ResponsePageDto.from(page);
            responsePageDto.setLikeCount(likeService.countLikesForPage(page.getId()));
            responsePageDtoList.add(responsePageDto);
        }

        return responsePageDtoList;
    }

    //페이지 정보 수정
    @Transactional
    public ResponsePageDto updatePage(UpdatePageDto updatePageDto){
        Page page = getPageById(updatePageDto.getPageId());
        page.updatePage(updatePageDto.getTitle(), updatePageDto.getContent());

        ResponsePageDto responsePageDto = ResponsePageDto.from(page);
        responsePageDto.setLikeCount(likeService.countLikesForPage(page.getId()));

        return responsePageDto;
    }

    //페이지 삭제
    @Transactional
    public long deletePage(Long pageId, Long userId){
        validatePage(pageId, userId);
        pageRepository.deleteById(pageId);
        log.info("Page deleted: " + pageId);

        return pageId;
    }

    //페이지 연결하기
    @Transactional
    public ResponsePageDto linkPage(LinkPageDto linkPageDto){

        Long prevPageId = linkPageDto.getPrevPageId();
        Long nextPageId = linkPageDto.getNextPageId();

        Page prevPage = getPageById(prevPageId);
        Page linkedPage = getPageById(nextPageId);

        validatePage(prevPage.getId(), prevPage.getUser().getId());
        validatePage(linkedPage.getId(), linkedPage.getUser().getId());

        // 이전 페이지가 이미 다른 페이지와 연결되어 있는지 확인
        if (prevPage.getNextId() != -1) {
            throw new PageException(ResponseCode.PAGE_ALREADY_LINKED_BETWEEN);
        }

        if (linkedPage.getNextId() != -1 && prevPage.getPrevId() != -1) {
            throw new PageException(ResponseCode.PAGE_ALREADY_LINKED_END);
        }

        if (prevPage.getPrevId() != -1 && linkedPage.getNextId()!= -1) {
            throw new PageException(ResponseCode.PAGE_ALREADY_LINKED_SWITCHED);
        }

        // 첫 페이지 바로 다음에 연결
        if (prevPage.getRootId() == -1 && linkedPage.getRootId() == -1 && linkedPage.getNextId() == -1){
            prevPage.setNextId(linkedPage.getId());
            prevPage.setRootId(prevPage.getId());
            linkedPage.setRootId(prevPage.getId());
            linkedPage.setPrevId(prevPage.getId());

            pageRepository.save(prevPage);
            pageRepository.save(linkedPage);
        }
        // 이 경우는 기존의 rootPage == linkedPage, prevPage는 새로 연결된 페이지
        else if(linkedPage.getRootId() == linkedPage.getId() && linkedPage.getPrevId() == -1) {
            linkedPage.setPrevId(prevPage.getId());
            prevPage.setNextId(linkedPage.getId());
            prevPage.setRootId(prevPage.getId());

            updateRootInfoForAllLinkedPages(prevPage.getRootId(), linkedPage.getId());

            pageRepository.save(prevPage);
            pageRepository.save(linkedPage);
        }
        // 페이지 연결 순서가 세 번째 이상
        else {
            Page rootPage = pageRepository.findById(prevPage.getRootId())
                    .orElseThrow(() -> new PageException(ResponseCode.PAGE_NOT_FOUND));

            prevPage.setNextId(linkedPage.getId());
            linkedPage.setPrevId(prevPage.getId());
            linkedPage.setRootId(rootPage.getId());

            pageRepository.save(prevPage);
            pageRepository.save(linkedPage);
        }

        return ResponsePageDto.from(linkedPage);
    }




    private void updateRootInfoForAllLinkedPages(Long newRootId, Long startPageId) {
        Page currentPage = getPageById(startPageId);
        while (currentPage != null) {
            currentPage.setRootId(newRootId);
            try {
                pageRepository.save(currentPage);
                System.out.println("Page with ID " + currentPage.getId() + " saved with new root ID " + newRootId);
            } catch (Exception e) {
                System.err.println("Failed to save page with ID " + currentPage.getId() + ": " + e.getMessage());
                e.printStackTrace();
                break;
            }

            Long nextId = currentPage.getNextId();
            System.out.println("currentPage.getNextId() : " + nextId);
            if (nextId != -1) {
                currentPage = getPageById(nextId);
                if (currentPage == null) {
                    System.err.println("Failed to retrieve page with ID " + nextId);
                    break;
                }
            } else {
                currentPage = null;
            }
        }
        System.out.println("updateRootInfoForAllLinkedPages() finished");
    }


    //페이지 좋아요
    @Transactional
    public ResponsePageDto likePage(LikePageDto likePageDto, String accessToken){

        String userEmail = jwtTokenProvider.getUserEmail(accessToken);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
        Long userId = user.getId();

        Page page = getPageById(likePageDto.getPageId());
        Book book = getBookById(page.getBook().getId());
        Like like = Like.createLike(user, LikeType.PAGE, page.getId());

        scoreService.plusLikeScore(userId, page);
        rankingService.likeBook(book.getId(), book.getCategory());
        rankingService.likePage(page.getId(), page.getEmotion().getType());

        likeRepository.save(like);
        pageRepository.save(page);

        ResponsePageDto dto = ResponsePageDto.from(page);
        dto.setLikeCount(likeService.countLikesForPage(page.getId()));

        return dto;
    }

    //좋아요 해제
    @Transactional
    public ResponsePageDto dislikePage(LikePageDto likePageDto, String accessToken){

        String userEmail = jwtTokenProvider.getUserEmail(accessToken);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
        Long userId = user.getId();

        Page page = getPageById(likePageDto.getPageId());

        Like like = likeRepository.findByLikeTypeAndTargetIdAndUserId(LikeType.PAGE, likePageDto.getPageId(), userId)
                .orElseThrow(() -> new LikeException(ResponseCode.LIKE_NOT_FOUND));

        if(like == null){
            throw new LikeException(ResponseCode.LIKE_NOT_FOUND);
        }
        else{

            rankingService.unlikePage(page.getId(), page.getEmotion().getType());
            rankingService.unlikeBook(page.getBook().getId(), page.getBook().getCategory());

            likeRepository.delete(like);
            pageRepository.save(page);

            ResponsePageDto dto = ResponsePageDto.from(page);
            dto.setLikeCount(likeService.countLikesForPage(page.getId()));

            return dto;
        }


    }

    public List<Page> getPagesByHashtag(String hashtag) {
        return hashtagRepository.findPagesByHashtag(hashtag);
    }

    public org.springframework.data.domain.Page<ResponsePageDto> getPagesByHashtag(String hashtag, Pageable pageable) {
        org.springframework.data.domain.Page<Page> pages = hashtagRepository.findPagesByHashtag(hashtag, pageable);

        List<ResponsePageDto> responsePageDtoList = new ArrayList<>();

        for (Page page : pages) {
            ResponsePageDto responsePageDto = ResponsePageDto.from(page);
            responsePageDto.setLikeCount(likeService.countLikesForPage(page.getId()));
            responsePageDtoList.add(responsePageDto);
        }

        return new PageImpl<>(responsePageDtoList, pageable, pages.getTotalElements());
    }

    @Transactional
    public Page createPageWithHashtags(Page page, Set<String> hashtags) {
        Set<Hashtag> hashtagEntities = new HashSet<>();

        for (String tag : hashtags) {
            Hashtag hashtag = hashtagRepository.findByTag(tag)
                    .orElseGet(() -> hashtagRepository.save(new Hashtag(tag)));
            hashtagEntities.add(hashtag);
        }

        page.setHashtags(hashtagEntities);

        return pageRepository.save(page);
    }

    @Transactional
    public void setHashtagsToPage(Page page, Set<String> hashtags) {
        Set<Hashtag> existingHashtags = page.getHashtags();

        for (String tag : hashtags) {
            Hashtag existingHashtag = hashtagRepository.findByTag(tag)
                    .orElseGet(() -> hashtagRepository.save(new Hashtag(tag)));
            existingHashtags.add(existingHashtag);
        }

        page.setHashtags(existingHashtags);

    }



    @Transactional
    public Set<ResponsePageDto> parseTopRankedPaged(Set<String> pageIds){
        Set<ResponsePageDto> pageDetails = new LinkedHashSet<>();
        for (String pageId : pageIds){
            Page page = getPageById(Long.parseLong(pageId));
            ResponsePageDto responsePageDto = ResponsePageDto.from(page);
            responsePageDto.setLikeCount(likeService.countLikesForPage(page.getId()));
            pageDetails.add(responsePageDto);
        }
        return pageDetails;
    }

    public org.springframework.data.domain.Page<ResponsePageDto> getPagesCreatedWithinLast24Hours(Pageable pageable) {
        Pageable sortedByCreationDateDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        org.springframework.data.domain.Page<Page> pages = pageRepository.findAllCreatedWithinLast24Hours(twentyFourHoursAgo, sortedByCreationDateDesc);

        List<ResponsePageDto> responsePageDtoList = pages.stream()
                .map(ResponsePageDto::from)
                .collect(Collectors.toList());

        return new PageImpl<>(responsePageDtoList, sortedByCreationDateDesc, pages.getTotalElements());
    }

    public org.springframework.data.domain.Page<ResponsePageDto> getPagesCreatedWithinLast24HoursByEmotionAndHashtag(String emotionType,String hashTag, Pageable pageable) {
        Pageable sortedByCreationDateDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);

        //그냥 최근 것만 - 모든 정보가 비어 있다.
        if((emotionType == null || emotionType.isEmpty()) && (hashTag == null || hashTag.isEmpty())){
            org.springframework.data.domain.Page<Page> pages = pageRepository.findAllCreatedWithinLast24Hours(twentyFourHoursAgo, sortedByCreationDateDesc);
            List<ResponsePageDto> responsePageDtoList = new ArrayList<>();
            for (Page page : pages) {
                ResponsePageDto responsePageDto = ResponsePageDto.from(page);
                responsePageDto.setLikeCount(likeService.countLikesForPage(page.getId()));
                responsePageDtoList.add(responsePageDto);
            }
            return new PageImpl<>(responsePageDtoList, sortedByCreationDateDesc, pages.getTotalElements());

        }
        //값이 emotionType만 입력
        else if(hashTag == null || hashTag.isEmpty()){
            EmotionType emotion = EmotionType.getByCode(emotionType);
            org.springframework.data.domain.Page<Page> pages = pageRepository.findAllByEmotionTypeCreatedWithinLast24Hours(twentyFourHoursAgo, emotion, sortedByCreationDateDesc);
            List<ResponsePageDto> responsePageDtoList = new ArrayList<>();
            for (Page page : pages) {
                ResponsePageDto responsePageDto = ResponsePageDto.from(page);
                responsePageDto.setLikeCount(likeService.countLikesForPage(page.getId()));
                responsePageDtoList.add(responsePageDto);
            }
            return new PageImpl<>(responsePageDtoList, sortedByCreationDateDesc, pages.getTotalElements());
        }
        //값이 해시태그만 입력
        else if(emotionType == null || emotionType.isEmpty()){
            org.springframework.data.domain.Page<Page> pages = pageRepository.findAllByHashTagCreatedWithinLast24Hours(twentyFourHoursAgo, hashTag, sortedByCreationDateDesc);
            List<ResponsePageDto> responsePageDtoList = new ArrayList<>();
            for (Page page : pages) {
                ResponsePageDto responsePageDto = ResponsePageDto.from(page);
                responsePageDto.setLikeCount(likeService.countLikesForPage(page.getId()));
                responsePageDtoList.add(responsePageDto);
            }
            return new PageImpl<>(responsePageDtoList, sortedByCreationDateDesc, pages.getTotalElements());
        }
        //둘 다 입력
        else{
            EmotionType emotion = EmotionType.getByCode(emotionType);
            org.springframework.data.domain.Page<Page> pages = pageRepository.findAllByEmotionTypeAndHashTagCreatedWithinLast24Hours(twentyFourHoursAgo, emotion, hashTag, sortedByCreationDateDesc);
            List<ResponsePageDto> responsePageDtoList = new ArrayList<>();
            for (Page page : pages) {
                ResponsePageDto responsePageDto = ResponsePageDto.from(page);
                responsePageDto.setLikeCount(likeService.countLikesForPage(page.getId()));
                responsePageDtoList.add(responsePageDto);
            }
            return new PageImpl<>(responsePageDtoList, sortedByCreationDateDesc, pages.getTotalElements());
        }
    }

    



    @Transactional
    public List<Page> getUserPageList(Long userId){
        return pageRepository.findAllByUserId(userId);
    }

//    @Transactional
//    public List<Like> getUserLikeList(Long userId){
//        return likeRepository.findAllByUserId(userId);
//    }

    @Transactional
    public List<Book> getUserBookList(Long userId){
        return bookRepository.findAllByUserId(userId);
    }


    public String getDefaultVoice(Long pageId) {
        Page page = getPageById(pageId);

        var email = page.getDefaultVoiceUserMail();
        if(email == null) {
            throw new PageException(ResponseCode.DEFAULT_VOICE_NOT_ASSIGNED);
        }

        return email;
    }

    public String setDefaultVoice(Long pageId, String userEmail) {
        Page page = getPageById(pageId);
        page.setDefaultVoiceUserMail(userEmail);
        pageRepository.save(page);
        return page.getDefaultVoiceUserMail();
    }



    private User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }

    private Page getPageById(Long pageId){
        return pageRepository.findById(pageId)
                .orElseThrow(() -> new PageException(ResponseCode.PAGE_NOT_FOUND));
    }

    private Book getBookById(Long bookId){
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new PageException(ResponseCode.BOOK_NOT_FOUND));
    }



    private void validateUser(Long userId){
        if(!userRepository.existsById(userId)){
            throw new UserException(ResponseCode.USER_NOT_FOUND);
        }
    }

    private void validatePage(Long pageId){
        if(!pageRepository.existsById(pageId)){
            throw new PageException(ResponseCode.PAGE_NOT_FOUND);
        }

    }

    private void validatePage(Long pageId, Long userId){
        if(!pageRepository.existsById(pageId)){
            throw new PageException(ResponseCode.PAGE_NOT_FOUND);
        }
        if (!pageRepository.existsByIdAndUserId(pageId, userId)){ //페이지의 주인이 아닌지 확인
            throw new PageException(ResponseCode.PAGE_NOT_OWNED);
        }
    }


}
