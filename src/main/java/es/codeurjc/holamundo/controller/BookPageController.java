package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.service.Book;
import es.codeurjc.holamundo.service.BookList;
import es.codeurjc.holamundo.service.Review;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

@Controller
public class BookPageController {

    private BookList books;
    private int selectedBookID;

    //Constructor
    public BookPageController() {
        this.books = new BookList();
    }

    @GetMapping("/book/{bookID}")
    public String loadBookPage(Model model, @PathVariable int bookID) {
        this.selectedBookID = bookID;

        String bookTitle = books.getBook(bookID).getTitle();
        String bookAuthor = books.getBook(bookID).getAuthor();
        String bookDescription = books.getBook(bookID).getDescription();
        String bookImage = books.getBook(bookID).getImage();
        String bookDate = books.getBook(bookID).getRelease();
        String bookISBN = books.getBook(bookID).getISBN();
        String bookGenre = books.getBook(bookID).getGenre();
        String bookSeries = books.getBook(bookID).getSeries();
        int bookPageCount = books.getBook(bookID).getPageCount();
        String bookPublisher = books.getBook(bookID).getPublisher();

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

        Map<Integer, Review> reviews = books.getBook(selectedBookID).getReviews();

        for (int j = 0; j < reviews.size(); j++) {
            String reviewTitle = reviews.get(j).getTitle();
            int reviewRating = reviews.get(j).getRating();
            String reviewAuthor = reviews.get(j).getAuthor();
            String reviewContent = reviews.get(j).getContent();    
            
            model.addAttribute("reviewTitle"+j, reviewTitle);
            model.addAttribute("reviewRating"+j, reviewRating);
            model.addAttribute("reviewAuthor"+j, reviewAuthor);
            model.addAttribute("reviewContent"+j, reviewContent);
        }

        return "infoBookPage";
    }
}
