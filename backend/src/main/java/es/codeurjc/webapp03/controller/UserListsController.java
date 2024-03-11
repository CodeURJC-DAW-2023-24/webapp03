package es.codeurjc.webapp03.controller;

import es.codeurjc.webapp03.service.BookService;
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
    private BookService bookService;


    public boolean checkLoggedIn(HttpServletRequest request) {
        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            currentUsername = authentication.getName();
            return true;
        }
        return false;
    }

    @PostMapping("/book/{id}/add/read")
    public String addReadBook(@PathVariable long id, HttpServletRequest request) {

        checkLoggedIn(request);

        userService.moveBookToList(userService.getUser(currentUsername), bookService.getBook(id), "read");

        // Book to add (treat the optional)
        userService.moveBookToList(userService.getUser(currentUsername), bookService.getBook(id), "read");

        return "redirect:/book/" + id;
    }

    @PostMapping("/book/{id}/add/reading")
    public String addReadingBook(@PathVariable long id, HttpServletRequest request) {

        checkLoggedIn(request);

        // Book to add (treat the optional)
        userService.moveBookToList(userService.getUser(currentUsername), bookService.getBook(id), "reading");

        return "redirect:/book/" + id;
    }

    @PostMapping("/book/{id}/add/wanted")
    public String addWantedBook(@PathVariable long id, HttpServletRequest request) {

        checkLoggedIn(request);

        // Book to add (treat the optional)
        userService.moveBookToList(userService.getUser(currentUsername), bookService.getBook(id), "wanted");

        return "redirect:/book/" + id;
    }

    @PostMapping("/book/{id}/removeFromLists")
    public String removeBookFromLists(@PathVariable long id, HttpServletRequest request) {

        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            currentUsername = authentication.getName();
        }

        // Book to remove (treat the optional)
        userService.removeBookFromAllLists(userService.getUser(currentUsername), bookService.getBook(id));

        return "redirect:/book/" + id;
    }
}
