package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.service.Book;
import es.codeurjc.holamundo.service.BookList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class SearchResultsPageController {
    private BookList books;
    private ArrayList<Book> bookQueries;

    public SearchResultsPageController() {
        this.books = new BookList();
    }

    @GetMapping("/search")
    public String loadSearchResultsPage(Model model, String query) {
        bookQueries = books.getMatchingResults(query, 1, 4);
        model.addAttribute("searchInputQuery", query);
        model.addAttribute("bookQueries", bookQueries);

        return "searchResultsPage";
    }

    @PostMapping("/search")
    public ResponseEntity<ArrayList<Book>> loadSearchResultsPagePost() {
        return new ResponseEntity<>(books.getMatchingResults("a", 1, 4), HttpStatus.OK);
    }
}
