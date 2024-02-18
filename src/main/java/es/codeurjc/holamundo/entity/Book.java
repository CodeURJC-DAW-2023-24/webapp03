package es.codeurjc.holamundo.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Book {
    //Data structure that will store example books (ID, Book title, Author, Description, Image, Date of publication, ISBN, Genre, Series, Page count, Publisher)
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private long ID;
    private String title;

    @ManyToMany
    private List<Author> author = new ArrayList<>();

    private String description;
    private String image;
    private String releaseDate;
    private String ISBN;

    @ManyToOne
    private Genre genre;

    private String series;
    private int pageCount;
    private String publisher;
    @OneToMany(mappedBy = "book")
    private List<Review> reviews = new ArrayList<>();

    public Book() {
    }

    public Book(String title, String description, String image, String releaseDate, String ISBN, String series, int pageCount, String publisher) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.releaseDate = releaseDate;
        this.ISBN = ISBN;
        this.series = series;
        this.pageCount = pageCount;
        this.publisher = publisher;
    }

    private void loadReviews(List<Review> reviewList) {
        // Example reviews to test the functionality of the webpage

        /* General method to load all stored reviews for a specific book*/
        this.reviews = reviewList;
    }



    public List<Review> getReviews() {
        return this.reviews;
    }

    public List<Review> getReviewsRange(int start, int end) {

        if (end > this.reviews.size()) {
            end = this.reviews.size();
        }
        return this.reviews.subList(start, end);
    }

    public void setReview(int ID, Review review) {
        this.reviews.add(review);
    }

    // Getters for 'title'
    public String getTitle() {
        return this.title;
    }


    // Getter for 'author list'
    public List<Author> getAuthor() {
        return this.author;
    }


    // Getter for 'description'
    public String getDescription() {
        return this.description;
    }

    // Getter for 'image'
    public String getImage() {
        return this.image;
    }

    // Getter for 'release'
    public String getReleaseDate() {
        return this.releaseDate;
    }

    // Getter for 'ISBN'
    public String getISBN() {
        return this.ISBN;
    }

    // Getter for 'genre'
    public Genre getGenre() {
        return this.genre;
    }

    // Getter for 'series'
    public String getSeries() {
        return this.series;
    }

    // Getter for 'pageCount'
    public int getPageCount() {
        return this.pageCount;
    }

    // Getter for 'publisher'
    public String getPublisher() {
        return this.publisher;
    }

    // Getter for 'ID'
    public long getID() {
        return this.ID;
    }

    //Setters
    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setAuthor(List<Author> author) {
        this.author = author;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setReleaseDate(String release) {
        this.releaseDate = release;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void addAuthor(Author author) {
        this.author.add(author);
    }

    public void removeAuthor(Author author) {
        this.author.remove(author);
    }

    public void removeReview(Review review) {
        this.reviews.remove(review);
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


    public String toString() {
        return "Book[" +
                "ID=" + ID +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", genre='" + genre + '\'' +
                ", series='" + series + '\'' +
                ", pageCount=" + pageCount +
                ", publisher='" + publisher + '\'' +
                ']';
    }
}
