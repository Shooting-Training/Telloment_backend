package cau.capstone.backend.page.model;


import cau.capstone.backend.User.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Book_id")
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
    }



}
