package cau.capstone.backend.page.model;


import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Emotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EmotionType type;

    @Enumerated(EnumType.ORDINAL)
    private EmotionIntensity intensity;




    public Emotion() {
    }

    public Emotion(EmotionType type, EmotionIntensity intensity) {
        this.type = type;
        this.intensity = intensity;
    }

    // Getter and setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmotionType getType() {
        return type;
    }

    public void setType(EmotionType type) {
        this.type = type;
    }

    // New method to set type from String
    public void setTypeFromString(String type) {
        try {
            this.type = EmotionType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid EmotionType: " + type);
            // Handle the error as needed (e.g., log the error, set a default value, etc.)
        }
    }

    public EmotionIntensity getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = EmotionIntensity.values()[intensity];
    }

    @Override
    public String toString() {
        return "Emotion{" +
                "id=" + id +
                ", type=" + type +
                ", intensity=" + intensity +
                '}';
    }
}