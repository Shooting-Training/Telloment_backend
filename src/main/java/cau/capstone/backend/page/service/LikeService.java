package cau.capstone.backend.page.service;

import cau.capstone.backend.global.util.api.ResponseCode;
import cau.capstone.backend.global.util.exception.BookException;
import cau.capstone.backend.page.model.Book;
import cau.capstone.backend.page.model.Like;
import cau.capstone.backend.page.model.LikeType;
import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.model.repository.BookRepository;
import cau.capstone.backend.page.model.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final BookRepository bookRepository;

//    public List<Like> findLikesForPage(Long pageId) {
//        return likeRepository.findByLikeTypeAndTargetId(LikeType.PAGE, pageId);
//    }

    // 새로운 메소드: 특정 페이지의 총 좋아요 수를 얻는 메소드
    public int countLikesForPage(Long pageId) {
        return likeRepository.countByLikeTypeAndTargetId(LikeType.PAGE, pageId);
    }

    public int countLikesForBook(Long bookId) {
        return likeRepository.countByLikeTypeAndTargetId(LikeType.BOOK, bookId);
    }


    public int countTotalLikesForBook(Long bookId) {
        int total = 0;

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException(ResponseCode.BOOK_NOT_FOUND));

        List<Page> pages = book.getPages();
        for (Page page : pages) {
            total += countLikesForPage(page.getId());
        }

        total += countLikesForBook(bookId);

        return total;
    }

    public int countTotalLikesForUser(Long userId) {
        List<Book> books = bookRepository.findAllByUserId(userId);
        int total = 0;

        for (Book book : books) {
            total += countTotalLikesForBook(book.getId());
        }

        return total;
    }

    public void deleteBookLike(Book book) {
        likeRepository.deleteAllByLikeTypeAndTargetId(LikeType.BOOK, book.getId());
    }

    public void deletePageLike(Page page) {
        likeRepository.deleteAllByLikeTypeAndTargetId(LikeType.PAGE, page.getId());
    }
}
