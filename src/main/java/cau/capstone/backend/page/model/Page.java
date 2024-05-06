package cau.capstone.backend.page.model;


import cau.capstone.backend.User.model.User;
import cau.capstone.backend.global.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "page")
public class Page extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_id")
    private Long id;

    @Column(name= "page_title")
    private String title;

    @Lob
    @Column(name = "page_content")
    private String content;

    @Column(name = "modified")
    private boolean modified = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "root_id")
    private long rootId = -1; //루트 게시물의 id
    @Column(name = "prev_id")
    private long prevId = -1; //이전 게시물의
    @Column(name = "next_id")
    private long nextId = -1; //다음 게시물의 id

    //페이지에는 여러 개의 모멘트가 존재할 수 있음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToMany(mappedBy = "page", fetch = FetchType.LAZY)
    private List<Scrap> scraps = new ArrayList<>();

    @OneToMany(mappedBy = "page", fetch = FetchType.LAZY)
    private List<Like> likes = new ArrayList<>();


    @Column(name = "is_scrapped")
    private boolean isScrapped = false;


    //생성메서드
    public static Page createPage(User user,Book book, String title, String content){
        Page page = new Page();
        page.user = user;
        page.title = title;
        page.content = content;
        page.book = book;

        return page;
    }

    public void updatePage(String title, String content){
        this.title = title;
        this.content = content;

        LocalDateTime modifiedAt = LocalDateTime.now();
        setUpdatedAt(modifiedAt);

        this.modified = true;
    }

    public Page copyPage(User user, Book book){
        Page page = new Page();
        page.user = user;
        page.title = this.title;
        page.content = this.content;
        page.book = book;

        return page;
    }

    public void setRootId(Page page) { this.rootId = page.getRootId();}
    public void setRootId(long rootId) { this.rootId = rootId;}

    public void setPrevId(Page page) {this.prevId  = page.getId();}
    public void setPrevId(long prevId) {this.prevId = prevId;}

    public void setNextId(Page page) {this.nextId = page.getId();}
    public void setNextId(long nextId) {this.nextId = nextId;}


    public int getScrapCount() { return scraps.size(); }
    public int getLikeCount() { return likes.size(); }
    public boolean isScrapped() { return isScrapped; }


}
