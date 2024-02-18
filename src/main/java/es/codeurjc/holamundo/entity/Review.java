package es.codeurjc.holamundo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private long ID;
    private String title;
    private String author;
    private int rating;
    private String content;

    private int bookID;

    @JsonIgnore
    @ManyToOne
    private Book book;

    public Review() {
    }

    public Review(String title, String author, int rating, String content, int bookID) {
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.content = content;
        this.bookID = bookID;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getContent() {
        return this.content;
    }

    public int getRating() {
        return this.rating;
    }

    public int getBookID() {
        return this.bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    // Getter and setter for 'book'
    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String toString() {
        return "Review[ID=" + this.ID + ", title='" + this.title + "', author='" + this.author + "', rating=" + this.rating + ", content='" + this.content + "', bookID=" + this.bookID + "]";
    }
}
