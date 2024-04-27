package cau.capstone.backend.model.request;

import cau.capstone.backend.model.entity.RoleType;
import cau.capstone.backend.model.entity.User;
import cau.capstone.backend.model.entity.UserStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class UserRequest {

    Long userId;
    String loginId;
    String username;
    String password;
    UserStatus status;
    String email;
    RoleType roleType;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @Builder
    private UserRequest(Long userId, String loginId, String username, String password, UserStatus status, String email, RoleType roleType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.loginId = loginId;
        this.username = username;
        this.password = password;
        this.status = status;
        this.email = email;
        this.roleType = roleType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // factory method 선언
    public static UserRequest of(Long userId, String loginId, String username, String password, UserStatus status, String email, RoleType roleType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserRequest(
                userId, loginId, username, password, status, email, roleType, createdAt, updatedAt
        );
    }

    // entity -> request 변환 메서드
    public static UserRequest fromEntity(User entity) {
        return new UserRequest(
                entity.getUserId(),
                entity.getLoginId(),
                entity.getUserName(),
                entity.getPassword(),
                entity.getUserStatus(),
                entity.getEmail(),
                entity.getRoleType(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}