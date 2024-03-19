package es.codeurjc.webapp03.controller;

import es.codeurjc.webapp03.service.BookService;
import es.codeurjc.webapp03.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class APIUserListsController {

    // PUT will be used, understanding that what we are replacing are each list

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    // Add book to read list
    @PostMapping("/api/book/read/{id}")
    public void addReadBook(@PathVariable long id, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (userService.checkLoggedIn(request)) {
            userService.moveBookToList(userService.getUser(principal.getName()), bookService.getBook(id), "read");
        }
    }

    // Add book to reading list
    @PostMapping("/api/book/reading/{id}")
    public void addReadingBook(@PathVariable long id, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (userService.checkLoggedIn(request)) {
            userService.moveBookToList(userService.getUser(principal.getName()), bookService.getBook(id), "reading");
        }
    }

    // Add book to wanted list
    @PostMapping("/api/book/wanted/{id}")
    public void addWantedBook(@PathVariable long id, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (userService.checkLoggedIn(request)) {
            userService.moveBookToList(userService.getUser(principal.getName()), bookService.getBook(id), "wanted");
        }
    }

    // Remove book from all lists
    @DeleteMapping("/api/book/lists/{id}")
    public void removeBookFromLists(@PathVariable long id, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (userService.checkLoggedIn(request)) {
            userService.removeBookFromAllLists(userService.getUser(principal.getName()), bookService.getBook(id));
        }
    }
}
