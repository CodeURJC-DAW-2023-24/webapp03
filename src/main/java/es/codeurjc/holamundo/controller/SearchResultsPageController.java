package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.service.Book;
import es.codeurjc.holamundo.service.BookList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class SearchResultsPageController {
    private BookList books;

    public SearchResultsPageController() {
        this.books = new BookList();
    }

    @GetMapping("/search")
    public String loadSearchResultsPage(Model model, @RequestParam String query) {
        ArrayList<Book> bookQueries = books.getMatchingResults(query);
        model.addAttribute("searchInputQuery", query);
        model.addAttribute("bookQueries", bookQueries);

        return "searchResultsPage";
    }
}
