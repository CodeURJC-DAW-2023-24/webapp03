package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.User;
import es.codeurjc.holamundo.repository.BookRepository;
import es.codeurjc.holamundo.repository.ReviewRepository;
import es.codeurjc.holamundo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController { // This class will control the User DB operations

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

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        // Add some books to their lists

    }
}
