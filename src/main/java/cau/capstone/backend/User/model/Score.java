package cau.capstone.backend.User.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.EnumMap;
import java.util.Map;

@Entity
@NoArgsConstructor
@Table(name = "score")
@Setter
@Getter
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "trip_score")
    private int tripScore;
    @Column(name = "itnscience_score")
    private int itnscienceScore;
    @Column(name = "moviedrama_score")
    private int moviedramaScore;
    @Column(name = "humor_score")
    private int humorScore;
    @Column(name = "music_score")
    private int musicScore;
    @Column(name = "marriage_score")
    private int marriageScore;
    @Column(name = "romance_score")
    private int romanceScore;
    @Column(name = "cooking_score")
    private int cookingScore;
    @Column(name = "health_score")
    private int healthScore;
    @Column(name = "studying_score")
    private int studyingScore;
    @Column(name = "art_score")
    private int artScore;
    @Column(name = "animal_score")
    private int animalScore;
    @Column(name = "humanity_score")
    private int humanityScore;
    @Column(name = "literature_score")
    private int literatureScore;
    @Column(name = "finance_score")
    private int financeScore;


    public Score(User user){
        this.user = user;
        this.tripScore = 0;
        this.itnscienceScore = 0;
        this.moviedramaScore = 0;
        this.humorScore = 0;
        this.musicScore = 0;
        this.marriageScore = 0;
        this.romanceScore = 0;
        this.cookingScore = 0;
        this.healthScore = 0;
        this.studyingScore = 0;
        this.artScore = 0;
        this.animalScore = 0;
        this.humanityScore = 0;
        this.literatureScore = 0;
        this.financeScore = 0;
    }
}
