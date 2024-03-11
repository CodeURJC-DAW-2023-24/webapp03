package es.codeurjc.webapp03.controller;

import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.BookService;
import es.codeurjc.webapp03.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchResultsPageController {
    private List<Book> bookQueries;
    private List<User> userQueries;

    @Autowired
    private BookService bookService;

    @Autowired
    public UserService userService;

    public SearchResultsPageController() {
    }

    @GetMapping("/search")
    public String loadSearchResultsPage(Model model, @RequestParam boolean users, @RequestParam String query, HttpServletRequest request) throws SQLException {
        Authentication authentication = (Authentication) request.getUserPrincipal();
        model.addAttribute("user", authentication);
        model.addAttribute("userSearch", users);
        if (authentication != null) {
            String currentUsername = authentication.getName();
            User user = userService.getUser(currentUsername);
            user.setProfileImageString(user.blobToString(user.getProfileImageFile()));
            model.addAttribute("profileImageString", user.getProfileImageString());
            model.addAttribute("username", currentUsername);
        }

        if (!users) {
            Page<Book> filteredBooks = bookService.searchBook(query, PageRequest.of(0, 4));
            bookQueries = filteredBooks.getContent();

            for (int i = 0; i < bookQueries.size(); i++) {
                bookQueries.get(i).setImageString(bookQueries.get(i).blobToString(bookQueries.get(i).getImageFile()));
            }

            List<Double> ratings = new ArrayList<>();
            bookQueries.forEach((book) -> {
                List<Double> bookRatings = bookService.getRatings(book.getID());
                double averageRating = 0;
                if (bookRatings.size() > 0) {
                    for (Double rating : bookRatings) {
                        averageRating += rating;
                    }
                    averageRating /= bookRatings.size();
                }
                ratings.add(averageRating);
            });

            long maxBooks = filteredBooks.getTotalElements();
            model.addAttribute("searchInputQuery", query);
            model.addAttribute("bookQueries", bookQueries);
            model.addAttribute("maxBooks", maxBooks);
            model.addAttribute("ratings", ratings);

            //Admin
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
        } else {
            Page<User> filteredUsers = userService.getUsersPageable(query, PageRequest.of(0, 4));
            userQueries = filteredUsers.getContent();

            for (int i = 0; i < userQueries.size(); i++) {
                userQueries.get(i).setProfileImageString(userQueries.get(i).blobToString(userQueries.get(i).getProfileImageFile()));
            }

            long maxUsers = filteredUsers.getTotalElements();
            model.addAttribute("searchInputQuery", query);
            model.addAttribute("userQueries", userQueries);
            model.addAttribute("maxUsers", maxUsers);

        }

        return "searchResultsPage";
    }

    @GetMapping("/search/loadMore")
    public String loadSearchResultsPageBooks(@RequestParam boolean userSearch, @RequestParam String query, @RequestParam int page, Model model) throws SQLException {
        if (!userSearch) {
            Page<Book> filteredBooks = bookService.searchBook(query, PageRequest.of(page, 4));
            bookQueries = filteredBooks.getContent();

            for (int i = 0; i < bookQueries.size(); i++) {
                bookQueries.get(i).setImageString(bookQueries.get(i).blobToString(bookQueries.get(i).getImageFile()));
            }

            List<Double> ratings = new ArrayList<>();
            bookQueries.forEach((book) -> {
                List<Double> bookRatings = bookService.getRatings(book.getID());
                double averageRating = 0;
                if (bookRatings.size() > 0) {
                    for (Double rating : bookRatings) {
                        averageRating += rating;
                    }
                    averageRating /= bookRatings.size();
                }
                ratings.add(averageRating);
            });

            long maxBooks = filteredBooks.getTotalElements();
            model.addAttribute("book", bookQueries);
            model.addAttribute("maxBooks", maxBooks);
            model.addAttribute("ratings", ratings);

            return "searchResultsBookTemplate";
        } else {
            Page<User> filteredUsers = userService.getUsersPageable(query, PageRequest.of(page, 4));
            userQueries = filteredUsers.getContent();

            for (int i = 0; i < userQueries.size(); i++) {
                userQueries.get(i).setProfileImageString(userQueries.get(i).blobToString(userQueries.get(i).getProfileImageFile()));
            }

            long maxUsers = filteredUsers.getTotalElements();
            model.addAttribute("user", userQueries);
            model.addAttribute("maxUsers", maxUsers);

            return "searchResultsUserTemplate";
        }
    }
}
