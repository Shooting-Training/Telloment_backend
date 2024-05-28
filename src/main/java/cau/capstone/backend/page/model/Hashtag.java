package cau.capstone.backend.page.model;

import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@RequiredArgsConstructor
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;

    @ManyToMany(mappedBy = "hashtags")
    private Set<Page> pages = new HashSet<>();

    @ManyToMany(mappedBy = "hashtags")
    private Set<Book> books = new HashSet<>();

    public Hashtag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

}