package es.codeurjc.webapp03.controller;

import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.repository.BookRepository;
import es.codeurjc.webapp03.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserListsController {

    private String currentUsername;

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookRepository;


    @PostMapping("/book/{id}/add/read")
    public String addReadBook(@PathVariable long id, HttpServletRequest request) {

        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            currentUsername = authentication.getName();
        }

        // Add the book to the user's reading books and remove it from the read books and wanted books
        // Book to add (treat the optional)
        Book book = bookRepository.findByID(id);

        // add book to user's read books
        userService.addBookToList(userService.getUser(currentUsername), book, "read");

        // remove book from user's reading books
        userService.removeBookFromList(userService.getUser(currentUsername), book, "reading");

        // remove book from user's wanted books
        userService.removeBookFromList(userService.getUser(currentUsername), book, "wanted");

        return "redirect:/book/" + id;
    }

    @PostMapping("/book/{id}/add/reading")
    public String addReadingBook(@PathVariable long id, HttpServletRequest request) {

        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            currentUsername = authentication.getName();
        }

        // Add the book to the user's reading books and remove it from the read books and wanted books
        // Book to add (treat the optional)
        Book book = bookRepository.findByID(id);

        // add book to user's reading books
        userService.addBookToList(userService.getUser(currentUsername), book, "reading");

        // remove book from user's read books
        userService.removeBookFromList(userService.getUser(currentUsername), book, "read");

        // remove book from user's wanted books
        userService.removeBookFromList(userService.getUser(currentUsername), book, "wanted");

        return "redirect:/book/" + id;
    }

    @PostMapping("/book/{id}/add/wanted")
    public String addWantedBook(@PathVariable long id, HttpServletRequest request) {

        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            currentUsername = authentication.getName();
        }

        // Add the book to the user's reading books and remove it from the read books and wanted books
        // Book to add (treat the optional)
        Book book = bookRepository.findByID(id);

        // add book to user's wanted books
        userService.addBookToList(userService.getUser(currentUsername), book, "wanted");

        // remove book from user's reading books
        userService.removeBookFromList(userService.getUser(currentUsername), book, "reading");

        // remove book from user's read books
        userService.removeBookFromList(userService.getUser(currentUsername), book, "read");

        return "redirect:/book/" + id;
    }

    @PostMapping("/book/{id}/removeFromLists")
    public String removeBookFromLists(@PathVariable long id, HttpServletRequest request) {

        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            currentUsername = authentication.getName();
        }

        // Remove the book from the user's reading books, read books and wanted books
        // Book to remove (treat the optional)
        Book book = bookRepository.findByID(id);

        // remove book from user's wanted books
        userService.removeBookFromList(userService.getUser(currentUsername), book, "wanted");

        // remove book from user's reading books
        userService.removeBookFromList(userService.getUser(currentUsername), book, "reading");

        // remove book from user's read books
        userService.removeBookFromList(userService.getUser(currentUsername), book, "read");

        return "redirect:/book/" + id;
    }
}
