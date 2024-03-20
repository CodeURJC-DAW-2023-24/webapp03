package es.codeurjc.webapp03.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

@Entity
public class Review {

    public interface BasicInfo {
    }

    @JsonView(BasicInfo.class)
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private long ID;

    @JsonView(BasicInfo.class)
    private String title;

    @JsonView(BasicInfo.class)
    private String authorName;

    @JsonIgnore
    @ManyToOne
    private User author;

    @JsonView(BasicInfo.class)
    private int rating;

    @JsonView(BasicInfo.class)
    private String content;

    @JsonIgnore
    @ManyToOne
    private Book book;

    public Review() {
    }

    public Review(String title, User author, int rating, String content, Book book) {
        this.title = title;
        this.rating = rating;
        this.content = content;
        this.author = author;
        this.book = book;
        this.authorName = author.getUsername();
    }

    public User getUser() {
        return this.author;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author.getUsername();
    }

    public void setAuthor(User author) {
        this.author = author;
        this.authorName = author.getUsername();
    }

    public String getContent() {
        return this.content;
    }

    public int getRating() {
        return this.rating;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return "Review{ID=" + this.ID + ", title='" + this.title + "', author='" + this.author.getUsername() + "', rating=" + this.rating + ", content='" + this.content + "}";
    }
}
