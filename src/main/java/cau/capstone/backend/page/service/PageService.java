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
import com.amazonaws.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final BookRepository bookRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final ScoreService scoreService;
    private final LikeService likeService;
    private final RankingService rankingService;
    private final HashtagRepository hashtagRepository;


    @Transactional
    public ResponsePageDto getPage(String  accessToken, Long pageId) {

        String userEmail = jwtTokenProvider.getUserEmail(accessToken);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
        Long userId = user.getId();

        Page page = getPageById(pageId);
        page.setViewCount(page.getViewCount() + 1);
        pageRepository.save(page);

        rankingService.incrementViewCountPage(page.getId(), page.getEmotion().getType());
        rankingService.incrementViewCountBook(page.getBook().getId(), page.getBook().getCategory());
        scoreService.plusViewScore(userId, page);

        return ResponsePageDto.from(page);
}

    public org.springframework.data.domain.Page<ResponsePageDto> searchPagesWithKeywordAndPaging(String keyword, Pageable pageable) {
        org.springframework.data.domain.Page<Page> pages = pageRepository.findByKeywordWithPaging(keyword, pageable);

        List<ResponsePageDto> responsePageDtoList = new ArrayList<>();

        for (Page page : pages) {
            ResponsePageDto responsePageDto = ResponsePageDto.from(page);
            responsePageDto.setLikeCount(likeService.countLikesForPage(page.getId()));
            responsePageDtoList.add(responsePageDto);
        }

        return new PageImpl<>(responsePageDtoList, pageable, pages.getTotalElements());
    }


    public org.springframework.data.domain.Page<ResponsePageDto> findAllPages(Pageable pageable) {
        org.springframework.data.domain.Page<Page> pages = pageRepository.findAll(pageable);

        List<ResponsePageDto> responsePageDtoList = new ArrayList<>();

        for (Page page : pages) {
            ResponsePageDto responsePageDto = ResponsePageDto.from(page);
            responsePageDto.setLikeCount(likeService.countLikesForPage(page.getId()));
            responsePageDtoList.add(responsePageDto);
        }


        return new PageImpl<>(responsePageDtoList, pageable, pages.getTotalElements());
    }

    public org.springframework.data.domain.Page<ResponsePageDto> findPagesByEmotionType(EmotionType emotionType, Pageable pageable) {
        org.springframework.data.domain.Page<Page> pages = pageRepository.findByEmotionType(emotionType, pageable);

        List<ResponsePageDto> responsePageDtoList = new ArrayList<>();

        for (Page page : pages) {
            ResponsePageDto responsePageDto = ResponsePageDto.from(page);
            responsePageDto.setLikeCount(likeService.countLikesForPage(page.getId()));
            responsePageDtoList.add(responsePageDto);
        }

        return new PageImpl<>(responsePageDtoList, pageable, pages.getTotalElements());
    }

    @Transactional
    public long createPage(CreatePageDto createPageDto, String accessToken) {
        String userEmail = jwtTokenProvider.getUserEmail(accessToken);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));

        Book book = getBookById(createPageDto.getBookId());

        Page page = Page.createPage(user, book,  createPageDto.getTitle(), createPageDto.getContent());

        setHashtagsToPage(page, createPageDto.getHashtags());

        return pageRepository.save(page).getId();
    }

    //먼저 페이지를 생성하고, 페이지의 감정을 다시 반환받아 설정한다.
    @Transactional
    public ResponsePageDto setPageEmotion(Long pageId, String emotionCode, int emotionIntensity){
        Page page = getPageById(pageId);
        page.setEmotion(emotionCode, emotionIntensity);
        pageRepository.save(page);

        return ResponsePageDto.from(page);
    }

    @Transactional
    public List<ResponsePageDto> getPageList(Long userId){
        validateUser(userId);
        List<Page> pageList = pageRepository.findAllByUserId(userId);

        List<ResponsePageDto> responsePageDtoList = pageList.stream()
                .map(ResponsePageDto::from)
                .collect(Collectors.toList());

        return responsePageDtoList;
    }

    //페이지 정보 수정
    @Transactional
    public long updatePage(UpdatePageDto updatePageDto){
        Page page = getPageById(updatePageDto.getPageId());
        page.updatePage(updatePageDto.getTitle(), updatePageDto.getContent());

        return pageRepository.save(page).getId();
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
    public long linkPage(LinkPageDto linkPageDto){

        Long prevPageId = linkPageDto.getPrevPageId();
        Long nextPageId = linkPageDto.getNextPageId();

        Page prevPage = getPageById(prevPageId);
        Page linkedPage = getPageById(nextPageId);

        validatePage(prevPage.getId(), prevPage.getUser().getId()) ;
        validatePage(linkedPage.getId(), linkedPage.getUser().getId());

        //첫 페이지 바로 다음에 연결
        if (prevPage.getRootId() == -1){
            prevPage.setNextId(linkedPage);
            linkedPage.setRootId(prevPage);
            linkedPage.setPrevId(prevPage);

            pageRepository.save(prevPage);
            pageRepository.save(linkedPage);
        }

        //페이지 연결 순서가 세 번째 이상
        else if(prevPage.getRootId() != -1){
            Page rootPage = pageRepository.findById(prevPage.getRootId())
                    .orElseThrow(() -> new PageException(ResponseCode.PAGE_NOT_FOUND));

            prevPage.setNextId(linkedPage);
            linkedPage.setPrevId(prevPage);
            linkedPage.setRootId(rootPage);

            pageRepository.save(prevPage);
            pageRepository.save(linkedPage);
        }

        return linkedPage.getId();
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
        pageRepository.save(page);
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
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        org.springframework.data.domain.Page<Page> pages = pageRepository.findAllCreatedWithinLast24Hours(twentyFourHoursAgo, pageable);

        List<ResponsePageDto> responsePageDtoList = pages.stream()
                .map(ResponsePageDto::from)
                .collect(Collectors.toList());

        return new PageImpl<>(responsePageDtoList, pageable, pages.getTotalElements());
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
    public List<Scrap> getUserScrapList(Long userId){
        return scrapRepository.findAllByUserId(userId);
    }

    @Transactional
    public List<Book> getUserBookList(Long userId){
        return bookRepository.findAllByUserId(userId);
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

    private Scrap getScrapById(Long scrapId){
        return scrapRepository.findById(scrapId)
                .orElseThrow(() -> new ScrapException(ResponseCode.SCRAP_NOT_FOUND));
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

    private void validateScrap(Long scrapId, Long userId){
        if(!scrapRepository.existsById(scrapId)){
            throw new ScrapException(ResponseCode.SCRAP_NOT_FOUND);
        }
        if(!scrapRepository.existsByIdAndUserId(scrapId, userId)){ //유저가 스크랩 한 게 맞는지 확인
            throw new ScrapException(ResponseCode.SCARP_NOT_OWNED);
        }
    }


}
