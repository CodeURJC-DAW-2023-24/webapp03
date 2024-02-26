package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.Book;
import es.codeurjc.holamundo.entity.User;
import es.codeurjc.holamundo.repository.BookRepository;
import es.codeurjc.holamundo.repository.UserRepository;
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

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    public UserRepository userRepository;

    public SearchResultsPageController() {
    }

    @GetMapping("/search")
    public String loadSearchResultsPage(Model model, String query, HttpServletRequest request) throws SQLException {

        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            String currentUsername = authentication.getName();
            User user = userRepository.findByUsername(currentUsername);
            user.setProfileImageString(user.blobToString(user.getProfileImageFile()));
            model.addAttribute("profileImageString", user.getProfileImageString());
        }

        Page<Book> filteredBooks = bookRepository.searchBooks(query, PageRequest.of(0, 4));
        bookQueries = filteredBooks.getContent();

        for(int i=0;i<bookQueries.size();i++){
            bookQueries.get(i).setImageString(bookQueries.get(i).blobToString(bookQueries.get(i).getImageFile()));
        }

        List<Double> ratings = new ArrayList<>();
        bookQueries.forEach((book) -> {
            List<Double> bookRatings = bookRepository.getRatingsByBookId(book.getID());
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

        return "searchResultsPage";
    }

    @GetMapping("/search/loadMore")
    public String loadSearchResultsPageBooks(@RequestParam String query, @RequestParam int page, Model model) throws SQLException {

        Page<Book> filteredBooks = bookRepository.searchBooks(query, PageRequest.of(page, 4));
        bookQueries = filteredBooks.getContent();

        for(int i=0;i<bookQueries.size();i++){
            bookQueries.get(i).setImageString(bookQueries.get(i).blobToString(bookQueries.get(i).getImageFile()));
        }

        List<Double> ratings = new ArrayList<>();
        bookQueries.forEach((book) -> {
            List<Double> bookRatings = bookRepository.getRatingsByBookId(book.getID());
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
    }
}
