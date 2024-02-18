package es.codeurjc.holamundo.component;

import org.springframework.stereotype.Component;

@Component
public class ReviewC {
    private int ID;
    private String title;
    private String author;
    private int rating;
    private String content;

    private int bookID;

    public ReviewC() {
    }

    public ReviewC(int ID, String title, String author, int rating, String content, int bookID) {
        this.ID = ID;
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

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String toString() {
        return "Review{ID=" + this.ID + ", title='" + this.title + "', author='" + this.author + "', rating=" + this.rating + ", content='" + this.content + "', bookID=" + this.bookID + "}";
    }
}