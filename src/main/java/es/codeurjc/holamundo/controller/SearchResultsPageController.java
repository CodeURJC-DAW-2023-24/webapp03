package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.Author;
import es.codeurjc.holamundo.entity.Book;
import es.codeurjc.holamundo.repository.BookRepository;
import es.codeurjc.holamundo.service.BookList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchResultsPageController {
    private BookList books;
    private List<Book> bookQueries;

    @Autowired
    private BookRepository bookRepository;

    public SearchResultsPageController() {
        this.books = new BookList();
    }

    @GetMapping("/search")
    public String loadSearchResultsPage(Model model, String query) {
        this.books = new BookList();
        Page<Book> filteredBooks = bookRepository.searchBooks(query, PageRequest.of(0, 4));
        bookQueries = filteredBooks.getContent();

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
    public String loadSearchResultsPageBooks(@RequestParam String query, @RequestParam int page, Model model) {
        Page<Book> filteredBooks = bookRepository.searchBooks(query, PageRequest.of(page, 4));
        bookQueries = filteredBooks.getContent();

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
