package es.codeurjc.holamundo.service;

import es.codeurjc.holamundo.component.Review;

import java.util.*;

public class Book {
    //Data structure that will store example books (ID, Book title, Author, Description, Image, Date of publication, ISBN, Genre, Series, Page count, Publisher)
    private int ID;
    private String title;
    private String author;
    private String description;
    private String image;
    private String release;
    private String ISBN;
    private String genre;
    private String series;
    private int pageCount;
    private String publisher;
    private Map<Integer, Review> reviews;

    private ReviewList reviewList;

    public Book (int ID, String title, String author, String description, String image, String release, String ISBN, String genre, String series, int pageCount, String publisher) {
        this.reviews = new HashMap<>();

        this.ID = ID;
        this.title = title;
        this.author = author;
        this.description = description;
        this.image = image;
        this.release = release;
        this.ISBN = ISBN;
        this.genre = genre;
        this.series = series;
        this.pageCount = pageCount;
        this.publisher = publisher;

        this.reviewList = new ReviewList();
        this.loadReviews(reviews);
    }

    private void loadReviews(Map<Integer, Review> reviewMap) {
        // Example reviews to test the functionality of the webpage  

        /* General method to load all stored reviews for a specific book*/
        this.reviews = reviewList.getBookReviews(this.ID);
    }

    public List<Review> getReviews() {
        return new ArrayList<>(this.reviews.values());

    }

    public void setReview(int ID, Review review){
        this.reviews.put(ID, review);
    }

    // Getters for 'title'
    public String getTitle() {
        return this.title;
    }

    // Getter for 'author'
    public String getAuthor() {
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
    public String getRelease() {
        return this.release;
    }

    // Getter for 'ISBN'
    public String getISBN() {
        return this.ISBN;
    }

    // Getter for 'genre'
    public String getGenre() {
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
    public int getID() {
        return this.ID;
    }

    //Setters
    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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
}
