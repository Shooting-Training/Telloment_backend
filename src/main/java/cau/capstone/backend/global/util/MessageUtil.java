package cau.capstone.backend.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtil { // 반복되는 메시지의 형식을 저장하고 관리

    public static final String NOT_NULL = "값이 존재해야 합니다.";
    public static final String NOT_BLANK = "값이 비어있을 수 없습니다.";
    public static final String NOT_DUPLICATED = "중복된 값이 존재합니다.";
    public static final String NOT_FOUND = "값을 찾을 수 없습니다.";
    public static final String INVALID = "유효하지 않은 값입니다.";
    public static final String INVALID_TYPE = "유효하지 않은 타입입니다.";
    public static final String INVALID_LENGTH = "유효하지 않은 길이입니다.";
    public static final String INVALID_FORMAT = "유효하지 않은 형식입니다.";
    public static final String INVALID_DATE = "유효하지 않은 날짜입니다.";


    public static final String AGE_RANGE = "나이는 5 이상, 100 이하의 값을 입력해주세요.";
    public static final String GENDER_RANGE = "성별은 0(남자) 또는 1(여자)의 값을 입력해주세요.";

    public static final String TIME_STAMP = "addedTime";
}