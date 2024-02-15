package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.service.Book;
import es.codeurjc.holamundo.service.BookList;
import es.codeurjc.holamundo.service.UserBookLists;
import es.codeurjc.holamundo.service.UserList;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class ProfilePageController {
    private UserList users;
    private UserBookLists userBList;
    private BookList bookList;
    private ArrayList<Book> readedBooks;
    private ArrayList<Book> readingBooks;
    private ArrayList<Book> wantedBooks;

    public ProfilePageController() {
        this.users = new UserList();
        this.userBList = new UserBookLists();
        this.bookList = new BookList();
    }

    @GetMapping("/profile/{username}/**")
    public String loadProfilePage(Model model, @PathVariable String username) {

        //UserList
        String role = users.getUserInfo(username)[1];
        String alias = users.getUserInfo(username)[2];
        String description = users.getUserInfo(username)[3];
        String profileImage = users.getUserInfo(username)[4];
        String email = users.getUserInfo(username)[5];
        String password = users.getUserInfo(username)[6];

        model.addAttribute("username", username);
        model.addAttribute("alias", alias);
        model.addAttribute("role", role);
        model.addAttribute("description", description);
        model.addAttribute("profileImage", profileImage);
        model.addAttribute("email", email);
        model.addAttribute("password", password);

        //UserBookLists
        int nReadedBooks = userBList.getReadedList(username).length;
        int nReadingBooks = userBList.getReadingList(username).length;
        int nWantedBooks = userBList.getWantedList(username).length;

        this.readedBooks = new ArrayList<>();
        this.readingBooks = new ArrayList<>();
        this.wantedBooks = new ArrayList<>();

        for (int idBook : userBList.getReadedList(username)) {
            readedBooks.add(bookList.getBook(idBook));
        }
        for (int idBook : userBList.getReadingList(username)) {
            readingBooks.add(bookList.getBook(idBook));
        }
        for (int idBook : userBList.getWantedList(username)) {
            wantedBooks.add(bookList.getBook(idBook));
        }

        model.addAttribute("nReadedBooks", nReadedBooks);
        model.addAttribute("nReadingBooks", nReadingBooks);
        model.addAttribute("nWantedBooks", nWantedBooks);
        model.addAttribute("ReadedBooks", readedBooks);
        model.addAttribute("ReadingBooks", readingBooks);
        model.addAttribute("WantedBooks", wantedBooks);

        return "profilePage";
    }

    @PostMapping("/profile/{username}/loadMore")
    public ResponseEntity<ArrayList<Book>> loadReadedBooks(@PathVariable String username, @RequestParam(defaultValue = "default") String listType) {
        if (listType.equals("readed")) {
            ArrayList<Integer> readedBooks = userBList.getRangeList(0, 4, "readed", username);
            ArrayList<Book> readedBooksToReturn = new ArrayList<>();
            for (int idBook : readedBooks) {
                readedBooksToReturn.add(bookList.getBook(idBook));
            }
            return new ResponseEntity<>(readedBooksToReturn, HttpStatus.OK);

        } else if (listType.equals("reading")) {
            ArrayList<Integer> readingBooks = userBList.getRangeList(0, 4, "reading", username);
            ArrayList<Book> readingBooksToReturn = new ArrayList<>();
            for (int idBook : readingBooks) {
                readingBooksToReturn.add(bookList.getBook(idBook));
            }
            return new ResponseEntity<>(readingBooksToReturn, HttpStatus.OK);

        } else if (listType.equals("wanted")) {
            ArrayList<Integer> wantedBooks = userBList.getRangeList(0, 4, "wanted", username);
            ArrayList<Book> wantedBooksToReturn = new ArrayList<>();
            for (int idBook : wantedBooks) {
                wantedBooksToReturn.add(bookList.getBook(idBook));
            }
            return new ResponseEntity<>(wantedBooksToReturn, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
