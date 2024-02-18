package es.codeurjc.holamundo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Genre {
    @Id
    private String name;

    @OneToMany(mappedBy = "genre")
    private List<Book> books = new ArrayList<>();

    public Genre() {
    }

    public Genre(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public List<Book> getBooks() {
        return this.books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        this.books.add(book);
    }

    public void removeBook(Book book) {
        this.books.remove(book);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
