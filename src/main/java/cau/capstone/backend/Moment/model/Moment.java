package cau.capstone.backend.Moment.model;


import cau.capstone.backend.User.model.User;
import cau.capstone.backend.global.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "moment")
public class Moment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moment_id")
    private Long id;

    @Column(name= "moment_title")
    private String title;

    @Lob
    @Column(name = "moment_content")
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
    @JoinColumn(name = "page_id")
    private Page page;

    @OneToMany(mappedBy = "moment", fetch = FetchType.LAZY)
    private List<Scrap> scraps = new ArrayList<>();

    @OneToMany(mappedBy = "moment", fetch = FetchType.LAZY)
    private List<Like> likes = new ArrayList<>();


    @Column(name = "is_scrapped")
    private boolean isScrapped = false;


    //생성메서드
    public static Moment createMoment(User user, String title, String content, Page page){
        Moment moment = new Moment();
        moment.user = user;
        moment.title = title;
        moment.content = content;
        moment.page = page;
        return moment;
    }

    public void updateMoment(String title, String content, LocalDateTime modifiedAt){
        this.title = title;
        this.content = content;
        super.updatedAt = modifiedAt;
        this.modified = true;
    }

    public void setCreatedDate(LocalDateTime date) { super.createdAt = date; }

    public void setRootId(Moment moment) { this.rootId = moment.getRootId();}
    public void setRootId(long rootId) { this.rootId = rootId;}

    public void setPrevId(Moment moment) {this.prevId  = moment.getId();}
    public void setPrevId(long prevId) {this.prevId = prevId;}

    public void setNextId(Moment moment) {this.nextId = moment.getId();}
    public void setNextId(long nextId) {this.nextId = nextId;}

}
