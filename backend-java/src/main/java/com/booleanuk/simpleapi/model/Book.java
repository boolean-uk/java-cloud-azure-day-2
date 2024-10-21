package com.booleanuk.simpleapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "is_borrowed")
    private boolean isBorrowed;

    public Book(String title, String author, boolean isBorrowed) {
        this.title = title;
        this.author = author;
        this.isBorrowed = isBorrowed;
    }

    public Book(int id) {
        this.id = id;
    }

    @JsonIgnore
    public boolean isValid() {
        return !(StringUtils.isBlank(title)
                || StringUtils.isBlank(author));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        return Objects.equals(id, book.id)
                && Objects.equals(title, book.title)
                && Objects.equals(author, book.author)
                && Objects.equals(isBorrowed, book.isBorrowed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, isBorrowed);
    }
}
