package es.codeurjc.holamundo.service;

import es.codeurjc.holamundo.component.ReviewC;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReviewList {
    // Data structure that will store all the reviews, asociated to a bookID

    private Map<Integer, ReviewC> reviews = new HashMap<>(); // Map<bookID, Review>

    // Constructor
    public ReviewList() {
        // Example reviews for book with ID 1
        for (int i = 0; i < 18; i++) {
            this.reviews.put(i, new ReviewC(i, "Review " + i, "Author " + i, i, "Content " + i, 1));
        }

        // Example reviews for book with ID 2
        for (int i = 18; i < 36; i++) {
            this.reviews.put(i, new ReviewC(i, "Review " + i, "Author " + i, i, "Content " + i, 2));
        }

        // Example reviews for book with ID 3
        for (int i = 36; i < 54; i++) {
            this.reviews.put(i, new ReviewC(i, "Review " + i, "Author " + i, i, "Content " + i, 3));
        }

        // Example reviews for book with ID 4
        for (int i = 54; i < 72; i++) {
            this.reviews.put(i, new ReviewC(i, "Review " + i, "Author " + i, i, "Content " + i, 4));
        }

        // Example reviews for book with ID 5
        for (int i = 72; i < 90; i++) {
            this.reviews.put(i, new ReviewC(i, "Review " + i, "Author " + i, i, "Content " + i, 5));
        }

        // Example reviews for book with ID 6
        for (int i = 90; i < 108; i++) {
            this.reviews.put(i, new ReviewC(i, "Review " + i, "Author " + i, i, "Content " + i, 6));
        }

        // Example reviews for book with ID 7
        for (int i = 108; i < 126; i++) {
            this.reviews.put(i, new ReviewC(i, "Review " + i, "Author " + i, i, "Content " + i, 7));
        }

        // Example reviews for book with ID 8
        for (int i = 126; i < 144; i++) {
            this.reviews.put(i, new ReviewC(i, "Review " + i, "Author " + i, i, "Content " + i, 8));
        }


    }

    public Map<Integer, ReviewC> getReviews() {
        return this.reviews;
    }

    public void setReview(ReviewC review) {
        this.reviews.put(this.reviews.size() + 1, review);
    }

    public ReviewC getReview(int bookID, int reviewID) {
        return this.reviews.get(bookID);
    }

    public void removeReview(int bookID, int reviewID) {
        this.reviews.remove(bookID);
    }

    public void updateReview(int bookID, int reviewID, ReviewC review) {
        this.reviews.put(bookID, review);
    }

    public int getSize() {
        return this.reviews.size();
    }

    // Get reviews for a specific book
    public Map<Integer, ReviewC> getBookReviews(int bookID) {
        Map<Integer, ReviewC> bookReviews = new HashMap<>();
        for (int i = 0; i < this.reviews.size(); i++) {
            if (this.reviews.get(i).getBookID() == bookID) {
                bookReviews.put(i, this.reviews.get(i));
            }
        }
        return bookReviews;
    }

}
