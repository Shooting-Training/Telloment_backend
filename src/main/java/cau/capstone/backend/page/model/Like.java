package cau.capstone.backend.page.model;


import cau.capstone.backend.User.model.User;
import cau.capstone.backend.global.BaseImmutableEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "like")
public class Like extends BaseImmutableEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;


    public static Like createLike(User user, Page page) {
        Like like = new Like();
        like.user = user;
        like.page = page;
        return like;
    }

}
