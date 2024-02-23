package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.Book;
import es.codeurjc.holamundo.repository.BookRepository;
import es.codeurjc.holamundo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserListsController {

    private String currentUsername = "FanBook_785"; //This is the username of the current user. This is just for testing purposes

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;


    @PostMapping("/book/{id}/add/read")
    public String addReadBook(@PathVariable long id) {
        // Add the book to the user's reading books and remove it from the read books and wanted books
        // Book to add (treat the optional)
        Book book = bookRepository.findByID(id);

        // add book to user's read books
        userRepository.findByUsername(currentUsername).addReadBook(book);
        userRepository.save(userRepository.findByUsername(currentUsername));

        // remove book from user's reading books
        userRepository.findByUsername(currentUsername).removeReadingBook(book);
        userRepository.save(userRepository.findByUsername(currentUsername));

        // remove book from user's wanted books
        userRepository.findByUsername(currentUsername).removeWantedBook(book);
        userRepository.save(userRepository.findByUsername(currentUsername));

        return "redirect:/book/" + id;
    }

    @PostMapping("/book/{id}/add/reading")
    public String addReadingBook(@PathVariable long id) {
        // Add the book to the user's reading books and remove it from the read books and wanted books
        // Book to add (treat the optional)
        Book book = bookRepository.findByID(id);

        // add book to user's reading books
        userRepository.findByUsername(currentUsername).addReadingBook(book);
        userRepository.save(userRepository.findByUsername(currentUsername));

        // remove book from user's read books
        userRepository.findByUsername(currentUsername).removeReadBook(book);
        userRepository.save(userRepository.findByUsername(currentUsername));

        // remove book from user's wanted books
        userRepository.findByUsername(currentUsername).removeWantedBook(book);
        userRepository.save(userRepository.findByUsername(currentUsername));

        return "redirect:/book/" + id;
    }

    @PostMapping("/book/{id}/add/wanted")
    public String addWantedBook(@PathVariable long id) {
        // Add the book to the user's reading books and remove it from the read books and wanted books
        // Book to add (treat the optional)
        Book book = bookRepository.findByID(id);

        // add book to user's wanted books
        userRepository.findByUsername(currentUsername).addWantedBook(book);
        userRepository.save(userRepository.findByUsername(currentUsername));

        // remove book from user's reading books
        userRepository.findByUsername(currentUsername).removeReadingBook(book);
        userRepository.save(userRepository.findByUsername(currentUsername));

        // remove book from user's read books
        userRepository.findByUsername(currentUsername).removeReadBook(book);
        userRepository.save(userRepository.findByUsername(currentUsername));

        return "redirect:/book/" + id;
    }

    @PostMapping("/book/{id}/removeFromLists")
    public String removeBookFromLists(@PathVariable long id) {
        // Remove the book from the user's reading books, read books and wanted books
        // Book to remove (treat the optional)
        Book book = bookRepository.findByID(id);

        // remove book from user's wanted books
        userRepository.findByUsername(currentUsername).removeWantedBook(book);
        userRepository.save(userRepository.findByUsername(currentUsername));

        // remove book from user's reading books
        userRepository.findByUsername(currentUsername).removeReadingBook(book);
        userRepository.save(userRepository.findByUsername(currentUsername));

        // remove book from user's read books
        userRepository.findByUsername(currentUsername).removeReadBook(book);
        userRepository.save(userRepository.findByUsername(currentUsername));

        return "redirect:/book/" + id;
    }
}
