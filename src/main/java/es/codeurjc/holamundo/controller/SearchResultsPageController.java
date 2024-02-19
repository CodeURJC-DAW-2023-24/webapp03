package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.Book;
import es.codeurjc.holamundo.repository.BookRepository;
import es.codeurjc.holamundo.service.BookC;
import es.codeurjc.holamundo.service.BookList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchResultsPageController {
    private BookList books;
    private ArrayList<BookC> bookQueries;

    @Autowired
    private BookRepository bookRepository;
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
        List<Book> bookListDB = bookRepository.searchBooks(query, PageRequest.of(0, 4)).getContent();
        bookListDB.forEach((book) ->{
            System.out.print(book + "\n");
        });
        return new ResponseEntity<>(books.getMatchingResults(query, 4, this.books.getSize()), HttpStatus.OK);
    }
}
