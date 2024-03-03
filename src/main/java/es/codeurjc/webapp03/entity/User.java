package es.codeurjc.webapp03.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import javax.sql.rowset.serial.SerialBlob;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Entity
public class User {

    @Id
    private String username;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    private String alias;

    private String description;

    @Lob
    @JsonIgnore
    private Blob profileImageFile;

    private String profileImageString;

    private String email;

    private String password;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>(); // reviews a user has made


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

    public User(String username, String alias, String description, String profileImage, String email, String password, String... roles) throws SQLException, IOException {
        this.username = username;
        this.roles = List.of(roles);
        this.alias = alias;
        this.description = description;
        if (profileImageString == null) {
            this.profileImageString = "/assets/defaultProfilePicture.png";
        } else {
            this.profileImageString = profileImage;
        }
        this.profileImageFile = LocalImageToBlob(profileImageString);
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public List<String> getRole() {
        return this.roles;
    }

    public String getAlias() {
        return this.alias;
    }

    public String getDescription() {
        return this.description;
    }

    public Blob getProfileImageFile() { return this.profileImageFile; }

    public String getProfileImageString() {
        return this.profileImageString;
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

    public void setRole(List<String> role) {
        this.roles = role;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProfileImageFile(Blob profileImageFile) { this.profileImageFile = profileImageFile; }

    public void setProfileImageString(String profileImageString) {
        this.profileImageString = profileImageString;
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

    public String blobToString(Blob blob) throws SQLException {
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        String userImage = Base64.getEncoder().encodeToString(bytes);
        return userImage;
    }

    public Blob LocalImageToBlob(String imagePath) throws IOException, SQLException {
            imagePath = imagePath.replace("/assets", "src/main/resources/static/assets");
            File fi = new File(imagePath);
            byte[] byteContent = Files.readAllBytes(fi.toPath());
            Blob imageBlob = new SerialBlob(byteContent);
            return imageBlob;
    }

    public void addReadBook(Book book) {
        // check if the book is already in the read books list
        if (!this.readBooks.contains(book)) {
            this.readBooks.add(book);
        }
    }

    public void addReadingBook(Book book) {
        // check if the book is already in the reading books list
        if(!this.readingBooks.contains(book)){
            this.readingBooks.add(book);
        }
    }

    public void addWantedBook(Book book) {
        // check if the book is already in the wanted books list
        if (!this.wantedBooks.contains(book)) {
            this.wantedBooks.add(book);
        }
    }

    public void removeReadBook(Book book) {
        // check if the book is in the read books list
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

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void removeReview(Review review) {
        this.reviews.remove(review);
    }

    public void clearReviews() {
        this.reviews.clear();
    }

    public List<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String toString() {
        return "User[username=" + this.getUsername() + ", role=" + this.getRole() + ", alias=" + this.getAlias() + ", description=" + this.getDescription() + ", profileImageString=" + this.getProfileImageString() + ", email=" + this.getEmail() + ", password=" + this.getPassword() + ", readBooks=" + this.getReadBooks() + ", readingBooks=" + this.getReadingBooks() + ", wantedBooks=" + this.getWantedBooks() + "]";
    }
}
