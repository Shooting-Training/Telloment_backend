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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    @Column(name = "view_count")
    private int viewCount = 0;

//    @Column(name = "emotion")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "emotion_id", referencedColumnName = "id")
    private Emotion emotion;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "page_hashtags",
            joinColumns = @JoinColumn(name = "page_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
    private Set<Hashtag> hashtags = new HashSet<>();

    @Column(name = "deafult_voice_user_mail")
    private String defaultVoiceUserMail;

    //생성메서드
    public static Page createPage(User user,Book book, String title, String content){
        Page page = new Page();
        page.user = user;
        page.title = title;
        page.content = content;
        page.book = book;

        page.emotion = new Emotion();

        return page;
    }

    public void updatePage(String title, String content){
        this.title = title;
        this.content = content;

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

    @PreRemove
    private void preRemove() {
        // 해시태그와의 연관 관계를 제거
        for (Hashtag hashtag : hashtags) {
            hashtag.getPages().remove(this);
        }
        hashtags.clear();
    }

    public void setRootId(Page page) { this.rootId = page.getId();}
    public void setRootId(long rootId) { this.rootId = rootId;}

    public void setPrevId(Page page) {this.prevId  = page.getId();}
    public void setPrevId(long prevId) {this.prevId = prevId;}

    public void setNextId(Page page) {this.nextId = page.getId();}
    public void setNextId(long nextId) {this.nextId = nextId;}

    public Set<String> getHashtagsTag() {
        Set<String> hashtagSet = new HashSet<>();
        for (Hashtag hashtag : hashtags) {
            hashtagSet.add(hashtag.getTag());
        }
        return hashtagSet;
    }


    public void setEmotion(String emotion, int intensity) {
        this.emotion.setTypeFromString(emotion);
        this.emotion.setIntensity(intensity);
    }


    public boolean containsKeyword(String keyword) {
        // 제목이나 내용에 키워드가 포함되어 있는지 확인
        if (title != null && title.contains(keyword)) {
            return true;
        }

        if (content != null && content.contains(keyword)) {
            return true;
        }

        // 해시태그 목록에서 키워드를 포함하는지 확인
        for (Hashtag hashtag : hashtags) {
            if (hashtag.getTag().contains(keyword)) {
                return true;
            }
        }

        // 위 조건들에 해당하지 않으면 false 반환
        return false;
    }


}
