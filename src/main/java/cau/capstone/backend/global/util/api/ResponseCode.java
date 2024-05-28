package cau.capstone.backend.global.util.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ResponseCode {

    // 400 Bad Request
    BAD_REQUEST(HttpStatus.BAD_REQUEST, false, "잘못된 요청입니다."),

    // 401 Unauthorized
    TOKEN_VALIDATION_FAILURE(HttpStatus.UNAUTHORIZED, false, "토큰 검증 실패"),

    // 403 Forbidden
    FORBIDDEN(HttpStatus.FORBIDDEN, false, "권한이 없습니다."),


    // 404 Not Found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, false, "사용자를 찾을 수 없습니다."),
    FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, false, "즐겨찾기를 찾을 수 없습니다."),
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, false, "페이지를 찾을 수 없습니다."),
    SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, false, "스크랩을 찾을 수 없습니다."),
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, false, "페이지를 찾을 수 없습니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, false, "좋아요를 찾을 수 없습니다."),


    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, false, "허용되지 않은 메소드입니다."),
    SCARP_NOT_OWNED(HttpStatus.METHOD_NOT_ALLOWED, false, "스크랩한 사용자가 아닙니다."),
    PAGE_NOT_OWNED(HttpStatus.METHOD_NOT_ALLOWED, false, "작성한 사용자가 아닙니다."),
    BOOK_NOT_OWNED(HttpStatus.METHOD_NOT_ALLOWED, false, "작성한 사용자가 아닙니다."),

    // 409 Conflict
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, false, "이미 가입한 사용자입니다."),
    USER_EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, false, "이미 존재하는 이메일입니다."),
    USER_NAME_ALREADY_EXIST(HttpStatus.CONFLICT, false, "이미 존재하는 닉네임입니다."),
    SCRAP_ALREADY_EXIST(HttpStatus.CONFLICT, false, "이미 존재하는 스크랩입니다."),
    FOLLOWED_ALREADY(HttpStatus.CONFLICT, false, "이미 팔로우한 사용자입니다."),
    UNFOLLOWED_ALREADY(HttpStatus.CONFLICT, false, "이미 언팔로우한 사용자입니다."),
    SCRAP_ALREADY_SCRAPPED(HttpStatus.CONFLICT, false, "이미 스크랩된 페이지입니다."),


    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, "서버에 오류가 발생하였습니다."),

    // 200 OK
    USER_SIGNUP_SUCCESS(HttpStatus.OK, true, "사용자 회원가입 성공"),
    USER_READ_SUCCESS(HttpStatus.CREATED, true, "사용자 정보 조회 성공"),
    USER_UPDATE_SUCCESS(HttpStatus.OK, true, "사용자 정보 수정 성공"),
    USER_SEARCH_SUCCESS(HttpStatus.OK, true, "사용자 검색 성공"),
    USER_FOLLOW_SUCCESS(HttpStatus.OK, true, "사용자 팔로우 성공"),
    USER_UNFOLLOW_SUCCESS(HttpStatus.OK, true, "사용자 언팔로우 성공"),
    USER_LOGIN_SUCCESS(HttpStatus.OK, true, "사용자 로그인 성공"),
    USER_DELETE_SUCCESS(HttpStatus.OK, true, "사용자 삭제 성공"),

    PAGE_READ_SUCCESS(HttpStatus.OK, true, "페이지 조회 성공"),
    PAGE_UPDATE_SUCCESS(HttpStatus.OK, true, "페이지 수정 성공"),
    PAGE_DELETE_SUCCESS(HttpStatus.OK, true, "페이지 삭제 성공"),
    PAGE_LINK_SUCCESS(HttpStatus.OK, true, "페이지 연결 성공"),
    PAGE_LIKE_SUCCESS(HttpStatus.OK, true, "페이지 좋아요 성공"),
    PAGE_DISLIKE_SUCCESS(HttpStatus.OK, true, "페이지 좋아요 취소 성공"),
    PAGE_NOT_IN_BOOK(HttpStatus.OK, true, "북에 속한 페이지가 아닙니다."),


    SCRAP_CREATE_SUCCESS(HttpStatus.CREATED, true, "스크랩 생성 성공"),
    SCRAP_READ_SUCCESS(HttpStatus.OK, true, "스크랩 읽기 성공"),
    SCRAP_DELETE_SUCCESS(HttpStatus.OK, true, "스크랩 삭제 성공"),
    SCRAP_UPDATE_SUCCESS(HttpStatus.OK, true, "스크랩 수정 성공"),

    BOOK_CREATE_SUCCESS(HttpStatus.CREATED, true, "북 생성 성공"),
    BOOK_READ_SUCCESS(HttpStatus.OK, true, "북 읽기 성공"),
    BOOK_UPDATE_SUCCESS(HttpStatus.OK, true, "북 수정 성공"),
    BOOK_DELETE_SUCCESS(HttpStatus.OK, true, "북 삭제 성공"),

    CATEGORY_READ_SUCCESS(HttpStatus.OK, true, "카테고리 조회 성공"),



    TOKEN_CHECK_SUCCESS(HttpStatus.OK, true, "토큰 검증 완료"),
    TOKEN_REISSUE_SUCCESS(HttpStatus.OK, true, "토큰 재발급 완료"),


    // 201 Created
    USER_CREATE_SUCCESS(HttpStatus.CREATED, true, "사용자 생성 성공"),
    PAGE_CREATE_SUCCESS(HttpStatus.CREATED, true, "페이지 생성 성공"),

    //voice api response
    VOICE_CLONE_SUCCESS(HttpStatus.OK, true, "음성 복제 성공"),
    VOICE_EMOTION_SUCCESS(HttpStatus.OK, true, "페이지 감정 분석 성공"),
    VOICE_SPEECH_SUCCESS(HttpStatus.OK, true, "페이지 음성 생성 성공"),
    VOICE_LIST_SUCCESS(HttpStatus.OK, true, "음성 목록 조회 성공"),
    VOICE_SCRAP_SUCCESS(HttpStatus.OK, true, "음성 스크랩 성공"),
    VOICE_DELETE_SCRAP_SUCCESS(HttpStatus.OK, true, "음성 스크랩 삭제 성공"),

    VOICE_LIST_EMPTY(HttpStatus.NOT_FOUND, false, "음성 목록이 비어있습니다."),
    VOICE_CLONE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, false, "음성 복제 실패"),
    VOICE_EMOTION_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, false, "페이지 감정 분석 실패"),

    ;
    private final HttpStatus httpStatus;
    private final Boolean success;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}