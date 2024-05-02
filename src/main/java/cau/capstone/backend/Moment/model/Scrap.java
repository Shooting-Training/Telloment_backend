package cau.capstone.backend.Moment.model;


import cau.capstone.backend.User.model.User;
import cau.capstone.backend.global.BaseImmutableEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
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
    @JoinColumn(name = "moment_id", nullable = false)
    private Moment moment;

    public static Scrap createScrap(User user, Moment moment) {
        Scrap scrap = new Scrap();
        scrap.user = user;
        scrap.moment = moment;
        return scrap;
    }


    public static Moment createMomentFromScrap(User user, Scrap scrap, Page page) {
        LocalDateTime createdDate = LocalDateTime.now();
        Moment originalMoment = scrap.getMoment();
        Moment moment = Moment.createMoment(user, originalMoment.getTitle(), originalMoment.getContent(), page);

        moment.setScrapped(true);
        moment.setRootId(originalMoment.getId());
        moment.setCreatedDate(createdDate);

        return moment;
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
//    public Moment createMomentFromScrap(String title, String content, LocalDate date, long rootId, long prevId, long nextId) {
//        LocalDate createdate = LocalDate.now();
//        Moment moment = Moment.createMoment(title, content, this.user, this, date, rootId, prevId, nextId);
//        moments.add(moment);
//        return moment;
//    }

}
