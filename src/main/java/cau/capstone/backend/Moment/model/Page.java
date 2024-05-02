package cau.capstone.backend.Moment.model;


import cau.capstone.backend.User.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Page {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private List<Moment> moments;


    public static Page createPage(User user) {
        Page page = new Page();
        page.user = user;
        return page;
    }

    public void addMoment(Moment moment) {
        moments.add(moment);
    }



}
