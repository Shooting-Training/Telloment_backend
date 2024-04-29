package cau.capstone.backend.model.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Scrap {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrap_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "favoriteFood", cascade = CascadeType.PERSIST, orphanRemoval = false) //스크랩이 삭제되면 같이 삭제
    private List<Moment> moments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int count = 0; // 즐겨찾기 선택

    public void addCount(){
        this.count++;
    }

    //생성메서드
    public static Scrap createScrap(String name, User user) {
        Scrap scrap = new Scrap();
        scrap.name = name;
        scrap.user = user;
        return scrap;
    }

    public void updateScrap(String name) {
        this.name = name;
    }

    //스크랩 정보로 새 모멘트 생성
    public Moment createMomentFromScrap(String title, String content, LocalDate date, long rootId, long prevId, long nextId) {
        LocalDate createdate = LocalDate.now();
        Moment moment = Moment.createMoment(title, content, this.user, this, date, rootId, prevId, nextId);
        moments.add(moment);
        return moment;
    }

}
