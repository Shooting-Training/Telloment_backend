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
    MOMENT_NOT_FOUND(HttpStatus.NOT_FOUND, false, "모먼트를 찾을 수 없습니다."),
    SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, false, "스크랩을 찾을 수 없습니다."),


    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, false, "허용되지 않은 메소드입니다."),
    SCARP_NOT_OWNED(HttpStatus.METHOD_NOT_ALLOWED, false, "스크랩한 사용자가 아닙니다."),
    MOMENT_NOT_OWNED(HttpStatus.METHOD_NOT_ALLOWED, false, "작성한 사용자가 아닙니다."),

    // 409 Conflict
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, false, "이미 가입한 사용자입니다."),
    USER_NAME_ALREADY_EXIST(HttpStatus.CONFLICT, false, "이미 존재하는 닉네임입니다."),
    FOLLOWED_ALREADY(HttpStatus.CONFLICT, false, "이미 팔로우한 사용자입니다."),
    UNFOLLOWED_ALREADY(HttpStatus.CONFLICT, false, "이미 언팔로우한 사용자입니다."),
    SCRAP_ALREADY_SCRAPPED(HttpStatus.CONFLICT, false, "이미 스크랩된 모먼트입니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, "서버에 오류가 발생하였습니다."),

    // 200 OK
    USER_READ_SUCCESS(HttpStatus.CREATED, true, "사용자 정보 조회 성공"),
    USER_UPDATE_SUCCESS(HttpStatus.OK, true, "사용자 정보 수정 성공"),
    USER_SEARCH_SUCCESS(HttpStatus.OK, true, "사용자 검색 성공"),
    USER_FOLLOW_SUCCESS(HttpStatus.OK, true, "사용자 팔로우 성공"),
    USER_UNFOLLOW_SUCCESS(HttpStatus.OK, true, "사용자 언팔로우 성공"),
    USER_LOGIN_SUCCESS(HttpStatus.OK, true, "사용자 로그인 성공"),

    MOMENT_READ_SUCCESS(HttpStatus.OK, true, "모먼트 조회 성공"),
    MOMENT_UPDATE_SUCCESS(HttpStatus.OK, true, "모먼트 수정 성공"),
    MOMENT_DELETE_SUCCESS(HttpStatus.OK, true, "모먼트 삭제 성공"),
    MOMENT_LINK_SUCCESS(HttpStatus.OK, true, "모먼트 연결 성공"),

    SCRAP_CREATE_SUCCESS(HttpStatus.CREATED, true, "스크랩 생성 성공"),
    SCRAP_READ_SUCCESS(HttpStatus.OK, true, "스크랩 읽기 성공"),
    SCRAP_DELETE_SUCCESS(HttpStatus.OK, true, "스크랩 삭제 성공"),
    SCRAP_UPDATE_SUCCESS(HttpStatus.OK, true, "스크랩 수정 성공"),



    TOKEN_CHECK_SUCCESS(HttpStatus.OK, true, "토큰 검증 완료"),


    // 201 Created
    USER_CREATE_SUCCESS(HttpStatus.CREATED, true, "사용자 생성 성공"),
    MOMENT_CREATE_SUCCESS(HttpStatus.CREATED, true, "모먼트 생성 성공"),



    private final HttpStatus httpStatus;
    private final Boolean success;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}