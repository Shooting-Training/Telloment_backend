package cau.capstone.backend.voice.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VoiceScrapKey implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "voice_id")
    private Long voiceId;

    // default constructor
    public VoiceScrapKey() {
    }

    // parameterized constructor
    public VoiceScrapKey(Long userId, Long voiceId) {
        this.userId = userId;
        this.voiceId = voiceId;
    }

    // getters and setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getVoiceId() {
        return voiceId;
    }

    public void setVoiceId(Long voiceId) {
        this.voiceId = voiceId;
    }

    // equals and hashCode methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoiceScrapKey that = (VoiceScrapKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(voiceId, that.voiceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, voiceId);
    }
}

