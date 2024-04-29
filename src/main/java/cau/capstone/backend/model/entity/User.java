package cau.capstone.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.util.annotation.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name; //닉네임

    @JsonIgnore
    @Column(length = 100, nullable = false, unique = true)
    private String keyCode; // 로그인 식별키

    @Nullable
    private String image; // 프로필 사진 경로

    private int age; // 나이

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}) //유저가 탈퇴하면 같이 삭제
    private List<Moment> moments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}) //유저가 탈퇴하면 같이 삭제
    private List<Scrap> scraps = new ArrayList<>();


    // Jwt 전용 설정 (UserDetails 인터페이스 구현)

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Override   //사용자의 권한 목록 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return keyCode;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Jwt 전용 설정 종료

    //생성 메서드
    public static User createUser(String name, String keyCode, String image, int age) {
        User user = new User();
        user.name = name;
        user.keyCode = keyCode;
        user.image = image;
        user.age = age;

        return user;
    }

    public void updateUser(String name, String image, int age) {
        this.name = name;
        this.image = image;
        this.age = age;
    }




}
