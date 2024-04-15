package cau.capstone.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Accessors(chain = true)
@ToString(exclude = {"password", "matchingList"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @Column(unique = true)
    private String email;
    private String password;
    private String name;

    @Column(unique = true)
    private String nickName;

    private String urlPledgeRequest;
    private String urlPledgeResponse;



//    @Enumerated(EnumType.STRING)
//    @Column(name = "ROLE", nullable = false)
//    private RoleType role;


}