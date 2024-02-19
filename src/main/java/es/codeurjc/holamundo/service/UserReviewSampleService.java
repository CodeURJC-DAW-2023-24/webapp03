package es.codeurjc.holamundo.service;

import es.codeurjc.holamundo.entity.Review;
import es.codeurjc.holamundo.repository.BookRepository;
import es.codeurjc.holamundo.repository.ReviewRepository;
import es.codeurjc.holamundo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Service
public class UserReviewSampleService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostConstruct
    public void init() {
        // Add some reviews (BookReader_14)
        Review review1 = new Review("This book is bananas", userRepository.findByUsername("BookReader_14"), 5, "I love this book", bookRepository.findByTitle("The Hobbit"));
        Review review2 = new Review("Tolkien strikes again", userRepository.findByUsername("BookReader_14"), 4, "I like this book a lot", bookRepository.findByTitle("The Fellowship of the Ring"));
        Review review3 = new Review("The Two Towers, more like The Two Sides of the coin", userRepository.findByUsername("BookReader_14"), 3, "This book is meh", bookRepository.findByTitle("The Two Towers"));
        Review review4 = new Review("And the King did indeed return...", userRepository.findByUsername("BookReader_14"), 5, "I pretty much love this book", bookRepository.findByTitle("The Return of the King"));
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
        reviewRepository.save(review4);

        // Add some reviews (FanBook_785)
        Review review5 = new Review("I love this book", userRepository.findByUsername("FanBook_785"), 5, "This book is amazing", bookRepository.findByTitle("Harry Potter and the Philosopher's Stone"));
        Review review6 = new Review("I like this book a lot", userRepository.findByUsername("FanBook_785"), 4, "This book is great", bookRepository.findByTitle("Harry Potter and the Chamber of Secrets"));
        Review review7 = new Review("This book is meh", userRepository.findByUsername("FanBook_785"), 3, "This book is ok", bookRepository.findByTitle("Harry Potter and the Prisoner of Azkaban"));
        Review review8 = new Review("I pretty much love this book", userRepository.findByUsername("FanBook_785"), 5, "This book is amazing", bookRepository.findByTitle("Harry Potter and the Goblet of Fire"));
        Review review16 = new Review("This book is HORRENDOUS", userRepository.findByUsername("FanBook_785"), 1, "I PERSONALLY HATE this book", bookRepository.findByTitle("The Hobbit"));
        reviewRepository.save(review5);
        reviewRepository.save(review6);
        reviewRepository.save(review7);
        reviewRepository.save(review8);
        reviewRepository.save(review16);

        // Add some reviews (YourReader)
        Review review9 = new Review("This book is amazing", userRepository.findByUsername("YourReader"), 5, "I love this book", bookRepository.findByTitle("A Game of Thrones"));
        Review review10 = new Review("This book is great", userRepository.findByUsername("YourReader"), 4, "I like this book a lot", bookRepository.findByTitle("A Clash of Kings"));
        Review review11 = new Review("This book is ok", userRepository.findByUsername("YourReader"), 3, "This book is meh", bookRepository.findByTitle("A Storm of Swords"));
        Review review12 = new Review("This book is amazing", userRepository.findByUsername("YourReader"), 5, "I pretty much love this book", bookRepository.findByTitle("A Feast for Crows"));
        Review review13 = new Review("This book is great", userRepository.findByUsername("YourReader"), 4, "I like this book a lot", bookRepository.findByTitle("The Hobbit"));
        Review review14 = new Review("This book is ok", userRepository.findByUsername("YourReader"), 3, "This book is meh", bookRepository.findByTitle("The Fellowship of the Ring"));
        Review review15 = new Review("This book is amazing", userRepository.findByUsername("YourReader"), 5, "I pretty much love this book", bookRepository.findByTitle("The Two Towers"));
        reviewRepository.save(review9);
        reviewRepository.save(review10);
        reviewRepository.save(review11);
        reviewRepository.save(review12);
        reviewRepository.save(review13);
        reviewRepository.save(review14);
        reviewRepository.save(review15);
    }

}
