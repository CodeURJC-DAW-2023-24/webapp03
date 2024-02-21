package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.Author;
import es.codeurjc.holamundo.entity.Book;
import es.codeurjc.holamundo.entity.User;
import es.codeurjc.holamundo.repository.UserRepository;
import es.codeurjc.holamundo.service.BookC;
import es.codeurjc.holamundo.service.BookList;
import es.codeurjc.holamundo.service.UserBookLists;
import es.codeurjc.holamundo.service.UserList;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class ProfilePageController {

    @Autowired
    private UserRepository userRepository;
    private UserList users;
    private UserBookLists userBList;
    private BookList bookList;
    private ArrayList<BookC> readBooks;
    private ArrayList<BookC> readingBooks;
    private ArrayList<BookC> wantedBooks;

    public ProfilePageController() {
        this.users = new UserList();
        this.userBList = new UserBookLists();
        this.bookList = new BookList();
    }

    @GetMapping("/profile/{username}/**")
    public String loadProfilePage(Model model, @PathVariable String username) {

        // Get user from the database
        User user = userRepository.findByUsername(username);

        //User info
        String role = user.getRole();
        String alias = user.getAlias();
        String description = user.getDescription();
        String profileImage = user.getProfileImage();
        String email = user.getEmail();
        String password = user.getPassword();

        model.addAttribute("username", username);
        model.addAttribute("alias", alias);
        model.addAttribute("role", role);
        model.addAttribute("description", description);
        model.addAttribute("profileImage", profileImage);
        model.addAttribute("email", email);
        model.addAttribute("password", password);

        // Get user's book lists from the database
        //UserBookLists
        int nReadBooks = user.getReadBooks().size();
        int nReadingBooks = user.getReadingBooks().size();
        int nWantedBooks = user.getWantedBooks().size();

        List<Book> readBooksList = userRepository.getReadBooks(username, PageRequest.of(0, 4)).getContent();
        List<Book> readingBooksList = userRepository.getReadingBooks(username, PageRequest.of(0, 4)).getContent();
        List<Book> wantedBooksList = userRepository.getWantedBooks(username, PageRequest.of(0, 4)).getContent();

        model.addAttribute("nReadBooks", nReadBooks);
        model.addAttribute("nReadingBooks", nReadingBooks);
        model.addAttribute("nWantedBooks", nWantedBooks);
        model.addAttribute("ReadBooks", readBooksList);
        model.addAttribute("ReadingBooks", readingBooksList);
        model.addAttribute("WantedBooks", wantedBooksList);

        return "profilePage";
    }

    @PostMapping("/profile/{username}/loadMore")
    public ResponseEntity<ArrayList<BookC>> loadReadBooks(@PathVariable String username, @RequestParam(defaultValue = "default") String listType) {
        if (listType.equals("read")) {
            ArrayList<Integer> readBooks = userBList.getRangeList(0, 4, "read", username);
            ArrayList<BookC> readBooksToReturn = new ArrayList<>();
            for (int idBook : readBooks) {
                readBooksToReturn.add(bookList.getBook(idBook));
            }
            return new ResponseEntity<>(readBooksToReturn, HttpStatus.OK);

        } else if (listType.equals("reading")) {
            ArrayList<Integer> readingBooks = userBList.getRangeList(0, 4, "reading", username);
            ArrayList<BookC> readingBooksToReturn = new ArrayList<>();
            for (int idBook : readingBooks) {
                readingBooksToReturn.add(bookList.getBook(idBook));
            }
            return new ResponseEntity<>(readingBooksToReturn, HttpStatus.OK);

        } else if (listType.equals("wanted")) {
            ArrayList<Integer> wantedBooks = userBList.getRangeList(0, 4, "wanted", username);
            ArrayList<BookC> wantedBooksToReturn = new ArrayList<>();
            for (int idBook : wantedBooks) {
                wantedBooksToReturn.add(bookList.getBook(idBook));
            }
            return new ResponseEntity<>(wantedBooksToReturn, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}