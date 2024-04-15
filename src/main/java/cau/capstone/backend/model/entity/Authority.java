package cau.capstone.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"user"})
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long authorityId;

    private String name;

    @JoinColumn(name = "user")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;


    public void setUser(User user) {
        this.user = user;
    }
}