package cau.capstone.backend.page.model;

import java.util.HashMap;
import java.util.Map;

public enum EmotionType {
    ANGER("분노", "ANGER"),
    SADNESS("슬픔", "SADNESS"),
    HAPPINESS("행복", "HAPPINESS"),
    NEUTRAL("중립", "NEUTRAL");

    private final String description;
    private final String code;

    private static final Map<String, EmotionType> BY_CODE = new HashMap<>();
    private static final Map<String, EmotionType> BY_DESCRIPTION = new HashMap<>();

    static {
        for (EmotionType e : values()) {
            BY_CODE.put(e.code, e);
            BY_DESCRIPTION.put(e.description, e);
        }
    }


    public static EmotionType getByCode(String code) {
        return BY_CODE.get(code);
    }

    public static EmotionType getByDescription(String name) {
        return BY_DESCRIPTION.get(name);
    }

    EmotionType(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

}