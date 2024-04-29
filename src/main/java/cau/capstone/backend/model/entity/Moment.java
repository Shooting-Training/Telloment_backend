package cau.capstone.backend.model.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Moment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moment_id")
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}) // 다대일로 매핑하여
    @JoinColumn(name = "scrap_id")
    private Scrap scrap;

    private LocalDate date;

    @CreatedDate
    @Column(name = "added_time") //테이블과 매핑
    private LocalDateTime addedTime; //클라이언트에서 추가하도록 요청 보낸 timestamp


    private long rootId; //루트 게시물의 id
    private long prevId; //이전 게시물의 id
    private long nextId; //다음 게시물의 id



    //생성메서드
    public static Moment createMoment(String title, String content, User user, Scrap scrap, LocalDate date, long rootId, long prevId, long nextId) {
        Moment moment = new Moment();
        moment.title = title;
        moment.content = content;
        moment.user = user;
        moment.scrap = scrap;
        moment.date = date;

        moment.rootId = rootId;
        moment.prevId = prevId;
        moment.nextId = nextId;

        return moment;
    }

    public static void updateMoment(Moment moment, String title, String content, LocalDate date) {
        moment.title = title;
        moment.content = content;
        moment.date = date;
    }

    public boolean isScrapped() {
        return this.scrap != null;
    }

    public void setScrap(Scrap scrap){
        this.scrap = scrap;
    }
}
