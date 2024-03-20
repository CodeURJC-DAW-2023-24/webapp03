package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.entity.Genre;
import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.BookService;
import es.codeurjc.webapp03.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class APIUserListsController {

    // PUT will be used, understanding that what we are replacing are each list

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    // Book existence checker
    private boolean bookExists(long id) {
        return bookService.getBook(id) != null;
    }

    // Add book to read list
    @JsonView(Book.BasicInfo.class)
    @PostMapping("/api/book/read/{id}")
    public ResponseEntity<Book> addReadBook(@PathVariable long id, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        // Check if book exists
        if (!bookExists(id)) {
            return ResponseEntity.notFound().build();
        }

        if (userService.checkLoggedIn(request)) {
            userService.moveBookToList(userService.getUser(principal.getName()), bookService.getBook(id), "read");
            return ResponseEntity.ok(bookService.getBook(id));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // Add book to reading list
    @JsonView(Book.BasicInfo.class)
    @PostMapping("/api/book/reading/{id}")
    public ResponseEntity<Book> addReadingBook(@PathVariable long id, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        // Check if book exists
        if (!bookExists(id)) {
            return ResponseEntity.notFound().build();
        }

        if (userService.checkLoggedIn(request)) {
            userService.moveBookToList(userService.getUser(principal.getName()), bookService.getBook(id), "reading");
            return ResponseEntity.ok(bookService.getBook(id));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // Add book to wanted list
    @JsonView(Book.BasicInfo.class)
    @PostMapping("/api/book/wanted/{id}")
    public ResponseEntity<Book> addWantedBook(@PathVariable long id, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        // Check if book exists
        if (!bookExists(id)) {
            return ResponseEntity.notFound().build();
        }

        if (userService.checkLoggedIn(request)) {
            userService.moveBookToList(userService.getUser(principal.getName()), bookService.getBook(id), "wanted");
            return ResponseEntity.ok(bookService.getBook(id));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // Remove book from all lists

    @JsonView(Book.BasicInfo.class)
    @DeleteMapping("/api/book/lists/{id}")
    public ResponseEntity<Book> removeBookFromLists(@PathVariable long id, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        // Check if book exists
        if (!bookExists(id)) {
            return ResponseEntity.notFound().build();
        }

        if (userService.checkLoggedIn(request)) {
            userService.removeBookFromAllLists(userService.getUser(principal.getName()), bookService.getBook(id));
            return ResponseEntity.ok(bookService.getBook(id));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get a user's list

    @JsonView(Book.BasicInfo.class)
    @GetMapping("/api/books/me")
    public ResponseEntity<?> getUserLists(HttpServletRequest request,
                                            @RequestParam("list") String list,
                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "size", defaultValue = "10") int size){

            Principal principal = request.getUserPrincipal();

            if (principal == null) {
                return ResponseEntity.status(401).build();
            } else {
                List<Book> booksFromList = userService.getListPageable(userService.getUser(principal.getName()), list, PageRequest.of(page, size));
                return ResponseEntity.ok(booksFromList);
            }
    }


    // TODO: Move this to a different controller (this is for testing)
    @JsonView(User.BasicInfo.class)
    @GetMapping("/api/user/me")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return ResponseEntity.status(401).build();
        } else {
            return ResponseEntity.ok(userService.getUser(principal.getName()));
        }
    }
}
