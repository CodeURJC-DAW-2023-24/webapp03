package es.codeurjc.holamundo.service;

import java.util.HashMap;
import java.util.Map;

public class Book {
    //Data structure that will store example books (ID, Book title, Author, Description, Image, Date of publication, ISBN, Genre, Series, Page count, Publisher)
    private String title;
    private String author;
    private String description;
    private String image;
    private Date release;
    private int ISBN;
    private String genre;
    private String series;
    private int pageCount;
    private String publisher;
    private Map<Integer, Review> reviews;

    public Book (String title, String author, String description, String image, Date release, int ISBN, String genre, String series, int pageCount, String publisher) {
        this.reviews = new HashMap<>();

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

        this.loadReviews(reviews);
    }

    private void loadReviews(HashMap<Integer, String[]> reviewMap) {
        // Example reviews to test the functionality of the webpage
        Review placeholder = new Review("title", "author", rating, "content");
        reviewMap.put(0, placeholder);
        reviewMap.put(1, placeholder);
        reviewMap.put(2, placeholder);
        reviewMap.put(3, placeholder);

        /* General method to load all storaged reviews
        for(int i=0; i<1000; i++){
            this.setReview(new Review("title", "author", rating, "content"));
        }
        */
    }

    public Map<Integer, Review> getReviews() {
        return reviews;
    }

    public void setReview(Review review){
        this.reviews.put(review)
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
    public Date getRelease() {
        return this.release;
    }

    // Getter for 'ISBN'
    public int getISBN() {
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
}
