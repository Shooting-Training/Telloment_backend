package cau.capstone.backend.User.model;

import java.util.HashMap;
import java.util.Map;

public enum Category {
    TRIP("TRIP", "trip"),
    ITNSCIENCE("ITNCSI", "IT and Science"),
    MOVIEDRAMA("MVD", "Movie and Drama"),
    HUMOR("HUMR", "Humor"),
    MUSIC("MUS", "Music"),
    MARRIAGE("MRG", "Marriage"),
    ROMANCE("ROM", "Romance"),
    COOKING("COK","Cooking"),
    HEALTH("HLTH", "Health"),
    STUDYING("STD", "Studying"),
    ART("ART", "Art"),
    ANIMAL("ANML", "Animal"),
    HUMANITY("HUMN", "Humanity"),
    LITERATURE("LIT", "Literature"),
    FINANCE("FIN", "Finance");

    private String code;
    private String name;


    private static final Map<String, Category> BY_CODE = new HashMap<>();
    private static final Map<String, Category> BY_NAME = new HashMap<>();

    static {
        for (Category c : values()) {
            BY_CODE.put(c.code, c);
            BY_NAME.put(c.name, c);
        }
    }

    Category(String code, String name){
        this.code = code;
        this.name = name;
    }

    // Getter for code
    public String getCode() {
        return code;
    }

    // Setter for code
    public void setCode(String code) {
        this.code = code;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Static method to get Category by code
    public static Category getByCode(String code) {
        return BY_CODE.get(code);
    }

    // Static method to get Category by name
    public static Category getByName(String name) {
        return BY_NAME.get(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
