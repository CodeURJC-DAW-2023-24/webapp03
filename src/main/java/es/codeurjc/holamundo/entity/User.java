package es.codeurjc.holamundo.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    private String username;

    private String role;

    private String alias;

    private String description;

    private String profileImage;

    private String email;

    private String password;

    @ManyToMany
    @JoinTable(name = "user_read_books",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> readBooks = new ArrayList<>(); // books a user has read

    @ManyToMany
    @JoinTable(name = "user_reading_books",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> readingBooks = new ArrayList<>(); // books a user is currently reading

    @ManyToMany
    @JoinTable(name = "user_wanted_books",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> wantedBooks = new ArrayList<>(); // books a user wants to read

    public User() {
    }

    public User(String username, String role, String alias, String description, String profileImage, String email, String password) {
        this.username = username;
        this.role = role;
        this.alias = alias;
        this.description = description;
        this.profileImage = profileImage;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getRole() {
        return this.role;
    }

    public String getAlias() {
        return this.alias;
    }

    public String getDescription() {
        return this.description;
    }

    public String getProfileImage() {
        return this.profileImage;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Book> getReadBooks() {
        return this.readBooks;
    }

    public List<Book> getReadingBooks() {
        return this.readingBooks;
    }

    public List<Book> getWantedBooks() {
        return this.wantedBooks;
    }

    public void setReadBooks(List<Book> readBooks) {
        this.readBooks = readBooks;
    }

    public void setReadingBooks(List<Book> readingBooks) {
        this.readingBooks = readingBooks;
    }

    public void setWantedBooks(List<Book> wantedBooks) {
        this.wantedBooks = wantedBooks;
    }

    public void addReadBook(Book book) {
        this.readBooks.add(book);
    }

    public void addReadingBook(Book book) {
        this.readingBooks.add(book);
    }

    public void addWantedBook(Book book) {
        this.wantedBooks.add(book);
    }

    public void removeReadBook(Book book) {
        this.readBooks.remove(book);
    }

    public void removeReadingBook(Book book) {
        this.readingBooks.remove(book);
    }

    public void removeWantedBook(Book book) {
        this.wantedBooks.remove(book);
    }

    public void clearReadBooks() {
        this.readBooks.clear();
    }

    public void clearReadingBooks() {
        this.readingBooks.clear();
    }

    public void clearWantedBooks() {
        this.wantedBooks.clear();
    }

    public void clearAllBooks() {
        this.readBooks.clear();
        this.readingBooks.clear();
        this.wantedBooks.clear();
    }

    public String toString() {
        return "User[username=" + this.getUsername() + ", role=" + this.getRole() + ", alias=" + this.getAlias() + ", description=" + this.getDescription() + ", profileImage=" + this.getProfileImage() + ", email=" + this.getEmail() + ", password=" + this.getPassword() + ", readBooks=" + this.getReadBooks() + ", readingBooks=" + this.getReadingBooks() + ", wantedBooks=" + this.getWantedBooks() + "]";
    }


}