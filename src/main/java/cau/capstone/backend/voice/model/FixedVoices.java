package cau.capstone.backend.voice.model;


import java.util.Arrays;

public enum FixedVoices {

    /* 중립 : “CLOVA_simple”
     행복 : “CLOVA_happy_0”, 	“CLOVA_happy_1”,		“CLOVA_happy_2”
     슬픔 : “CLOVA_sad_0”, 		“CLOVA_sad_1”, 		“CLOVA_sad_2”
     분노 : “CLOVA_rage_0”, 	“CLOVA_rage_1”, 		“CLOVA_rage_2”

     make a enum class with the above values
     */
    CLOVA_SIMPLE("CLOVA_simple"),
    CLOVA_HAPPY_0("CLOVA_happy_0"),
    CLOVA_HAPPY_1("CLOVA_happy_1"),
    CLOVA_HAPPY_2("CLOVA_happy_2"),
    CLOVA_SAD_0("CLOVA_sad_0"),
    CLOVA_SAD_1("CLOVA_sad_1"),
    CLOVA_SAD_2("CLOVA_sad_2"),
    CLOVA_RAGE_0("CLOVA_rage_0"),
    CLOVA_RAGE_1("CLOVA_rage_1"),
    CLOVA_RAGE_2("CLOVA_rage_2")
    ;
    String value;

    FixedVoices(String value) {
        this.value = value;
    }

    public static FixedVoices fromValue(String value) {
        return Arrays.stream(FixedVoices.values())
                .filter(v -> v.value.equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
