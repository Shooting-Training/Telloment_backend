package cau.capstone.backend.page.service;

import cau.capstone.backend.User.model.User;
import cau.capstone.backend.User.model.repository.FollowRepository;
import cau.capstone.backend.User.model.repository.UserRepository;
import cau.capstone.backend.global.util.api.ResponseCode;
import cau.capstone.backend.global.util.exception.PageException;
import cau.capstone.backend.global.util.exception.ScrapException;
import cau.capstone.backend.global.util.exception.UserException;
import cau.capstone.backend.page.dto.request.CreatePageFromScrapDto;
import cau.capstone.backend.page.dto.request.CreateScrapDto;
import cau.capstone.backend.page.dto.request.DeleteScrapDto;
import cau.capstone.backend.page.dto.response.ResponseScrapDto;
import cau.capstone.backend.page.model.Book;
import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.model.Scrap;
import cau.capstone.backend.page.model.repository.BookRepository;
import cau.capstone.backend.page.model.repository.LikeRepository;
import cau.capstone.backend.page.model.repository.PageRepository;
import cau.capstone.backend.page.model.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class ScrapService {


    private final PageRepository pageRepository; //
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;



    //모먼트 스크랩
    @Transactional
    public Long saveScrap(CreateScrapDto createScrapDto){
        Page page = getPageById(createScrapDto.getPageId());

        boolean isScrapped = scrapRepository.existsByUserIdAndPageId(createScrapDto.getUserId(), createScrapDto.getPageId());

        if (isScrapped){
            throw new ScrapException(ResponseCode.SCRAP_ALREADY_EXIST);
        } else{
            User user = getUserById(createScrapDto.getUserId());
            //createScrap
            Scrap scrap = Scrap.createScrap(user, page);
            return scrapRepository.save(scrap).getId();
        }
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
        Page page = pageRepository.findById(scrap.getPage().getId())
                .orElseThrow(() -> new PageException(ResponseCode.PAGE_NOT_FOUND));
        page.getScraps().remove(scrap);

        scrapRepository.deleteById(scrapId);
        pageRepository.save(page);

        log.info("scrap deleted: " + scrapId);

    }


    //스크랩을 통해 페이지를 생성. 스크랩시 원하면 같은 내용의 페이지를 생성한다.
    @Transactional
    public Long createPageFromScrap(CreatePageFromScrapDto createPageFromScrapDto){
        validateScrap(createPageFromScrapDto.getScrapId(), createPageFromScrapDto.getUserId());


        Scrap scrap = getScrapById(createPageFromScrapDto.getScrapId());
        User user = getUserById(createPageFromScrapDto.getUserId());
        Book book = bookRepository.findById(createPageFromScrapDto.getBookId())
                .orElseThrow(() -> new PageException(ResponseCode.BOOK_NOT_FOUND));

        Page page = scrap.createPageFromScrap(user, scrap, book);

        return pageRepository.save(page).getId();
    }





    private User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }

    private Page getPageById(Long pageId){
        return pageRepository.findById(pageId)
                .orElseThrow(() -> new PageException(ResponseCode.PAGE_NOT_FOUND));
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

    private void validatePage(Long pageId, Long userId){
        if(!pageRepository.existsById(pageId)){
            throw new PageException(ResponseCode.PAGE_NOT_FOUND);
        }
        if (!pageRepository.existsByIdAndUserId(pageId, userId)){ //모먼트의 주인이 아닌지 확인
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
