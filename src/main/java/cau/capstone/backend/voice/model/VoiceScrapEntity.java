package cau.capstone.backend.voice.model;


import cau.capstone.backend.User.model.User;
import lombok.*;

import javax.persistence.*;


@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@Table(name = "voice_scrap")
public class VoiceScrapEntity {
    @EmbeddedId
    private VoiceScrapKey id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voice_id", referencedColumnName = "id", insertable = false, updatable = false)
    private VoiceEntity voice;

    // additional fields if any

    // default constructor
    public VoiceScrapEntity() {
    }

    // parameterized constructor
    public VoiceScrapEntity(VoiceScrapKey id) {
        this.id = id;
    }

    // getters and setters

}


