package cau.capstone.backend.page.model;


import cau.capstone.backend.User.model.User;
import cau.capstone.backend.global.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
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


    public static Book createBook(User user, String bookName) {
        Book book = new Book();
        book.user = user;
        book.bookName = bookName;


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
