//package cau.capstone.backend.model.entity;
//
//
//import cau.capstone.backend.model.AuditingFields;
//import jakarta.persistence.*;
//import lombok.*;
//
//
//@ToString(callSuper = true,exclude = {"password"})
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Table(name = "user")
//@Getter
//@Entity
//public class Moment extends AuditingFields {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "moment_id")
//    private long id;
//
//    @Column(name = "title", nullable = false)
//    private String title;
//
//    @Lob
//    private String content;
//
//    @Column(name = "root_id")
//    private long rootId;
//
//    @Column(name = "next_id")
//    private long nextId;
//
//    @Column(name = "like_count", columnDefinition = "integer default 0")
//    private int likeCount;
//
//    @Column(name = "like_count", columnDefinition = "integer default 0")
//    private int forkCount;
//
//
//}
