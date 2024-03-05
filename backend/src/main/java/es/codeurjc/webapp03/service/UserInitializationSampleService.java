package es.codeurjc.webapp03.service;

import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.repository.BookRepository;
import es.codeurjc.webapp03.repository.ReviewRepository;
import es.codeurjc.webapp03.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.sql.SQLException;

@Service
public class UserInitializationSampleService { // This class will control the User DB operations

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() throws SQLException, IOException {
        // Add some users
        List<String> userRoles = new ArrayList<>();
        userRoles.add("USER");

        List<String> adminRoles = new ArrayList<>();
        adminRoles.add("USER");
        adminRoles.add("ADMIN");

        User user1 = new User("BookReader_14", "BookReader", "I'm a reader fan that loves fantasy books", "", "bookreader14@gmail.com", passwordEncoder.encode("pass"), "USER");
        User user2 = new User("FanBook_785", "FanB", "I love books", "", "fanBook@gmail.com", passwordEncoder.encode("pass"), "USER");
        User user3 = new User("YourReader", "YourReader", "I'm a reader", "", "reader@gmail.com", passwordEncoder.encode("pass"), "USER");
        User admin = new User("AdminReader", "adminR", "I'm a Bookmarks admin", null, "adminReader@gmail.com", passwordEncoder.encode("adminpass"), "USER" , "ADMIN");

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
        userRepository.save(admin);
    }
}
