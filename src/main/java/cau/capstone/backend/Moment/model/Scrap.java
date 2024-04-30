package cau.capstone.backend.Moment.model;


import cau.capstone.backend.User.model.User;
import cau.capstone.backend.global.BaseImmutableEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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
    private Moment moments;

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
