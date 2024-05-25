package cau.capstone.backend.voice.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Setter
@Getter
@Entity
@Builder
@ToString
@Table(name = "voice_scrap")

public class VoiceScrapEntity {
    @EmbeddedId
    private VoiceScrapKey id;

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


