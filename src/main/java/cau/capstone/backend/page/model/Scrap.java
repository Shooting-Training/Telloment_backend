package cau.capstone.backend.page.model;


import cau.capstone.backend.User.model.User;
import cau.capstone.backend.global.BaseImmutableEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Scrap extends BaseImmutableEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrap_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", nullable = false)
    private Page page;

    public static Scrap createScrap(User user, Page page) {
        Scrap scrap = new Scrap();
        scrap.user = user;
        scrap.page = page;
        return scrap;
    }


    public static Page createPageFromScrap(User user, Scrap scrap, Book book) {
        LocalDateTime createdDate = LocalDateTime.now();
        Page originalPage = scrap.getPage();
        Page page = Page.createPage(user, originalPage.getTitle(), originalPage.getContent(), book);

        page.setScrapped(true);
        page.setRootId(originalPage.getId());
        page.setCreatedDate(createdDate);

        return page;
    }
//
//
//    private int count = 0; // 즐겨찾기 선택
//
//    public void addCount(){
//        this.count++;
//    }
//
//    //생성메서드
//    public static Scrap createScrap(String name, User user) {
//        Scrap scrap = new Scrap();
//        scrap.name = name;
//        scrap.user = user;
//        return scrap;
//    }
//
//    public void updateScrap(String name) {
//        this.name = name;
//    }
//
//    //스크랩 정보로 새 모멘트 생성
//    public Page createPageFromScrap(String title, String content, LocalDate date, long rootId, long prevId, long nextId) {
//        LocalDate createdate = LocalDate.now();
//        Page Page = Page.createPage(title, content, this.user, this, date, rootId, prevId, nextId);
//        Pages.add(Page);
//        return Page;
//    }

}
