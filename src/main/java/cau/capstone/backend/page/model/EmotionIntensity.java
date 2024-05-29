package cau.capstone.backend.page.model;

public enum EmotionIntensity {
    ZERO(0),
    ONE(1),
    TWO(2);


    private final int intensity;

    EmotionIntensity(int intensity) {
        this.intensity = intensity;
    }

    public int getIntensity() {
        return intensity;
    }
}