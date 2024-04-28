package cau.capstone.backend.model.entity;

import cau.capstone.backend.model.AuditingFields;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;


@ToString(callSuper = true,exclude = {"password"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Getter
@Entity
public class User extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "login_id", nullable = false)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "email", nullable = false)
    private String  email;


    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleType roleType;

    @Column(name = "status", nullable = false)
    private UserStatus userStatus;

    // id, 생성일자, 수정일자는 자동으로 등록된다.
    @Builder
    private User(Long id, String loginId, String password, String username, String email, RoleType roleType, UserStatus userStatus) {
        this.userId = id;
        this.loginId = loginId;
        this.password = password;
        this.userName = username;
        this.email = email;
        this.roleType = roleType;
        this.userStatus = userStatus;
    }




}