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

    @Enumerated(EnumType.STRING)
    @Column(name = "like_type")
    private LikeType likeType;

    @Column(name = "target_id")
    private Long targetId;


    public static Like createLike(User user, LikeType likeType, Long targetId) {
        Like like = new Like();
        like.user = user;
        like.likeType = likeType;
        like.targetId = targetId;
        return like;
    }

}
