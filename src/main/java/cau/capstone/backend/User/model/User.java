package cau.capstone.backend.User.model;

import cau.capstone.backend.page.model.Like;
import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.model.Book;
import cau.capstone.backend.page.model.Scrap;
import cau.capstone.backend.global.Authority;
import cau.capstone.backend.global.BaseEntity;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User extends BaseEntity implements UserDetails  {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;


    @Column(name="user_email")
    private String email; //닉네임

    @Column(name="user_passwd")
    private String passwd; //비밀번호

    @Column(name="user_image")
    private String image; //프로필 이미지

    @Column(name="user_name")
    private String name; //이름

    @Column(name = "user_nickname")
    private String nickname; //닉네임

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Authority role = Authority.ROLE_USER; //권한
//
//    @Column(name="provider", columnDefinition = "varchar(10) default 'EMAIL'")
//    @Enumerated(EnumType.STRING)
//    private Provider provider = Provider.EMAIL; //로그인 제공자

    @Column
    private LocalDateTime lastLoginAt; //마지막 로그인 시간


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Page> pages = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Scrap> scraps = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "followee", fetch = FetchType.LAZY)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY)
    private List<Follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();


    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Score score = new Score();

    @Builder
    public User(String email, String passwd,Authority role) {
        this.email = email;
        this.passwd = passwd;
        this.role = role;
    }


    // Jwt 전용 설정 (UserDetails 인터페이스 구현)

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Authority> auth = Collections.singletonList(this.role);
        return auth.stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.passwd;
    }

    @Override
    public String getUsername(){
        return this.email;
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
    public static User createUser(String email, String passwd, String name, String nickname) {
        User user = new User();
        user.email = email;
        user.passwd = passwd;
        user.name = name;
        user.nickname = nickname;
        user.role = Authority.ROLE_USER;
        return user;
    }

    public void updateUser(String email, String image, String name, String nickname) {
        this.email = email;
        this.name = name;
        this.image = image;
        this.nickname = nickname;
    }

    public void addBook(Book book) {
        this.books.add(book);
    }

    public void setId(Long id) {
        this.id = id;
    }


}
