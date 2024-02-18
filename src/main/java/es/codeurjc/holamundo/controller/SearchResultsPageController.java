package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.service.BookC;
import es.codeurjc.holamundo.service.BookList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class SearchResultsPageController {
    private BookList books;
    private ArrayList<BookC> bookQueries;
    @GetMapping("/search")
    public String loadSearchResultsPage(Model model, String query) {
        this.books = new BookList();
        bookQueries = books.getMatchingResults(query, 1, 4);
        model.addAttribute("searchInputQuery", query);
        model.addAttribute("bookQueries", bookQueries);

        return "searchResultsPage";
    }

    @PostMapping("/search")
    public ResponseEntity<ArrayList<BookC>> loadSearchResultsPagePost(String query) {
        return new ResponseEntity<>(books.getMatchingResults(query, 4, this.books.getSize()), HttpStatus.OK);
    }
}
