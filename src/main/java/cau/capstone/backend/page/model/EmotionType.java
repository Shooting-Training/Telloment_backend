package cau.capstone.backend.page.model;

public enum EmotionType {
    ANGER("분노"),
    SADNESS("슬픔"),
    HAPPINESS("행복"),
    NEUTRAL("중립");

    private final String description;

    EmotionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}