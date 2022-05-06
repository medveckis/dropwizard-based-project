package io.medveckis.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Author extends Item {
    private String name;
    @Temporal(TemporalType.DATE)
    private Date bornOn;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "author_book",
            joinColumns = {@JoinColumn(name = "author_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id")}
    )
    List<Book> books;

    public Author() {
    }

    public Author(String name, Date bornOn) {
        this.name = name;
        this.bornOn = bornOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBornOn() {
        return bornOn;
    }

    public void setBornOn(Date bornOn) {
        this.bornOn = bornOn;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}