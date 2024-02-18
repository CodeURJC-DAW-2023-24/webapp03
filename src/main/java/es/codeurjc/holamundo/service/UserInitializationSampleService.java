package es.codeurjc.holamundo.service;

import es.codeurjc.holamundo.entity.User;
import es.codeurjc.holamundo.repository.BookRepository;
import es.codeurjc.holamundo.repository.ReviewRepository;
import es.codeurjc.holamundo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Service
public class UserInitializationSampleService { // This class will control the User DB operations

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostConstruct
    public void init() {
        // Add some users
        User user1 = new User("BookReader_14", "User", "BookReader", "I'm a reader fan that loves fantasy books", "", "bookreader14@gmail.com", "12345678");
        User user2 = new User("FanBook_785", "User", "FanB", "I love books", "", "fanBook@gmail.com", "12345678");
        User user3 = new User("YourReader", "User", "YourReader", "I'm a reader", "", "reader@gmail.com", "12345678");

        // Add some books to their lists

        // BookReader_14 ---
        // Read books
        user1.addReadBook(bookRepository.findByTitle("The Hobbit"));
        user1.addReadBook(bookRepository.findByTitle("The Fellowship of the Ring"));
        user1.addReadBook(bookRepository.findByTitle("The Two Towers"));

        // Reading books
        user1.addReadingBook(bookRepository.findByTitle("The Return of the King"));
        user1.addReadingBook(bookRepository.findByTitle("A Game of Thrones"));

        // Want to read books
        user1.addWantedBook(bookRepository.findByTitle("A Clash of Kings"));
        user1.addWantedBook(bookRepository.findByTitle("A Storm of Swords"));
        user1.addWantedBook(bookRepository.findByTitle("A Feast for Crows"));
        user1.addWantedBook(bookRepository.findByTitle("Harry Potter and the Philosopher's Stone"));
        user1.addWantedBook(bookRepository.findByTitle("Harry Potter and the Chamber of Secrets"));
        user1.addWantedBook(bookRepository.findByTitle("Harry Potter and the Prisoner of Azkaban"));

        // FanBook_785 ---
        // Read books
        user2.addReadBook(bookRepository.findByTitle("Harry Potter and the Philosopher's Stone"));
        user2.addReadBook(bookRepository.findByTitle("Harry Potter and the Chamber of Secrets"));
        user2.addReadBook(bookRepository.findByTitle("Harry Potter and the Prisoner of Azkaban"));
        user2.addReadBook(bookRepository.findByTitle("Harry Potter and the Goblet of Fire"));
        user2.addReadBook(bookRepository.findByTitle("A Game of Thrones"));
        user2.addReadBook(bookRepository.findByTitle("A Clash of Kings"));
        user2.addReadBook(bookRepository.findByTitle("A Storm of Swords"));
        user2.addReadBook(bookRepository.findByTitle("A Feast for Crows"));

        // Reading books
        user2.addReadingBook(bookRepository.findByTitle("The Return of the King"));

        // Want to read books
        user2.addWantedBook(bookRepository.findByTitle("The Hobbit"));
        user2.addWantedBook(bookRepository.findByTitle("The Fellowship of the Ring"));
        user2.addWantedBook(bookRepository.findByTitle("The Two Towers"));

        // YourReader ---
        // Read books
        user3.addReadBook(bookRepository.findByTitle("The Hobbit"));
        user3.addReadBook(bookRepository.findByTitle("The Fellowship of the Ring"));
        user3.addReadBook(bookRepository.findByTitle("The Two Towers"));
        user3.addReadBook(bookRepository.findByTitle("The Return of the King"));

        // Reading books
        user3.addReadingBook(bookRepository.findByTitle("A Game of Thrones"));
        user3.addReadingBook(bookRepository.findByTitle("A Clash of Kings"));
        user3.addReadingBook(bookRepository.findByTitle("A Storm of Swords"));

        // Want to read books
        user3.addWantedBook(bookRepository.findByTitle("A Feast for Crows"));
        user3.addWantedBook(bookRepository.findByTitle("Harry Potter and the Philosopher's Stone"));
        user3.addWantedBook(bookRepository.findByTitle("Harry Potter and the Chamber of Secrets"));
        user3.addWantedBook(bookRepository.findByTitle("Harry Potter and the Prisoner of Azkaban"));
        user3.addWantedBook(bookRepository.findByTitle("Harry Potter and the Goblet of Fire"));

        // Save the users
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }
}
