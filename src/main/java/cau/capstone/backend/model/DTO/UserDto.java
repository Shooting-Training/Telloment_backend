package cau.capstone.backend.model.DTO;


import cau.capstone.backend.model.entity.RoleType;
import cau.capstone.backend.model.entity.UserStatus;
import cau.capstone.backend.model.entity.User;
import cau.capstone.backend.model.request.UserRequest;

import java.time.LocalDateTime;

/**
 * DTO for {@link cau.capstone.backend.model.entity.User}
 */
public record UserDto(
        Long userId,
        String loginId,
        String username,
        String password,
        UserStatus status,
        String email,
        RoleType roleType,
//        List<UserDetailsDto> userDetails, // 계정에서의 참조는 없앤다.
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // factory method of 선언
    public static UserDto of(Long userId, String loginId, String username, String password, UserStatus status, String email, RoleType roleType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserDto(userId, loginId, username, password, status, email, roleType, createdAt, updatedAt);
    }

    // security에서 사용할 팩토리 메서드
    public static UserDto of(String loginId) {
        return new UserDto(
                null, loginId, null, null, null, null, null, null, null
        );
    }

    // Principal에서 사용할 factory method of 선언
    public static UserDto of(Long userId, String loginId, String username, String password, String email, RoleType roleType) {
        return new UserDto(
                userId, loginId, username, password, null, email, roleType, null, null
        );
    }

    // 서비스 레이어에서 entity를 dto로 변환시켜주는 코드
    public static UserDto fromEntity(User entity) {
        return UserDto.of(
                entity.getUserId(),
                entity.getLoginId(),
                entity.getUserName(),
                entity.getPassword(),
                entity.getUserStatus(),
                entity.getEmail(),
                entity.getRoleType(),
//                userDetails,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // request -> dto 변환 메서드
    public static UserDto fromRequest(UserRequest request) {
        return of(
                request.getUserId(),
                request.getLoginId(),
                request.getUsername(),
                request.getPassword(),
                request.getStatus(),
                request.getEmail(),
                request.getRoleType(),
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }

}
