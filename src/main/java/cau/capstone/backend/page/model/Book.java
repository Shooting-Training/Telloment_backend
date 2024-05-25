package cau.capstone.backend.page.model;


import cau.capstone.backend.User.model.User;
import cau.capstone.backend.global.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "book")
public class Book extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    private String bookName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private List<Page> pages;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "book_hashtags",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
    private Set<Hashtag> hashtags = new HashSet<>();

    public static Book createBook(User user, String bookName, String categoryCode) {
        Book book = new Book();
        book.user = user;
        book.bookName = bookName;
        book.category = Category.getByCode(categoryCode);


        return book;
    }

    public void addPage(Page page) {
        this.pages.add(page);

        setUpdatedAt(LocalDateTime.now());
    }

    public void removePage(Page page){
        this.pages.remove(page);
        setUpdatedAt(LocalDateTime.now());
    }



}
