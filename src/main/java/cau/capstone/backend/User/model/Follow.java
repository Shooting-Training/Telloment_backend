package cau.capstone.backend.User.model;

import cau.capstone.backend.global.BaseImmutableEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;



@Entity
@NoArgsConstructor
@Table(name = "follow")
public class Follow extends BaseImmutableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_user_id")
    private User followee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_user_id")
    private User follower;

    public static Follow createFollow(User followee, User follower) {
        Follow follow = new Follow();
        follow.setFollowee(followee);
        follow.setFollower(follower);

        return follow;
    }



    public void setFollowee(User followee) {
        this.followee = followee;
    }
    public void setFollower(User follower) {
        this.follower = follower;
    }

}