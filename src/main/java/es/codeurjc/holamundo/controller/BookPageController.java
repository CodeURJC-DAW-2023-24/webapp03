package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.service.BookList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

@Controller
public class BookPageController {

    private BookList books;

    //Constructor
    public BookPageController() {
        this.books = new BookList();
    }

    @GetMapping("/book/{bookID}")
    public String loadBookPage(Model model, @PathVariable int bookID) {
        String bookTitle = books.getBook(bookID)[1];
        String bookAuthor = books.getBook(bookID)[2];
        String bookDescription = books.getBook(bookID)[3];
        String bookImage = books.getBook(bookID)[4];
        String bookDate = books.getBook(bookID)[5];
        String bookISBN = books.getBook(bookID)[6];
        String bookGenre = books.getBook(bookID)[7];
        String bookSeries = books.getBook(bookID)[8];
        String bookPageCount = books.getBook(bookID)[9];
        String bookPublisher = books.getBook(bookID)[10];

        model.addAttribute("bookTitle", bookTitle);
        model.addAttribute("bookAuthor", bookAuthor);
        model.addAttribute("bookDescription", bookDescription);
        model.addAttribute("bookImage", bookImage);
        model.addAttribute("bookDate", bookDate);
        model.addAttribute("bookISBN", bookISBN);
        model.addAttribute("bookGenre", bookGenre);
        model.addAttribute("bookSeries", bookSeries);
        model.addAttribute("bookPageCount", bookPageCount);
        model.addAttribute("bookPublisher", bookPublisher);

        return "infoBookPage";
    }
}
