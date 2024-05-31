package cau.capstone.backend.page.model;

import cau.capstone.backend.global.util.api.ResponseCode;
import cau.capstone.backend.global.util.exception.EmotionException;

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
        EmotionType result = BY_CODE.get(code);
        if (result == null) {
            throw new EmotionException(ResponseCode.EMOTION_TYPE_NOT_FOUND);
        }
        return result;
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