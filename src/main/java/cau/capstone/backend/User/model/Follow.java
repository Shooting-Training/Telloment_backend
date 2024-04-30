package cau.capstone.backend.User.model;

import cau.capstone.backend.global.BaseImmutableEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "user_follower")
public class Follow extends BaseImmutableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @Id
    @Column(name = "to_user", insertable = false, updatable = false)
    private Long followee;

    @Id
    @Column(name = "from_user", insertable = false, updatable = false)
    private Long follower;

    public Follow createFollow(Long followee, Long follower) {
        Follow follow = new Follow();
        follow.followee = followee;
        follow.follower = follower;
        return follow;
    }

}