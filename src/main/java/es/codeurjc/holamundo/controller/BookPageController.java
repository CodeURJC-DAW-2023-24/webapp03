package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.Book;
import es.codeurjc.holamundo.entity.Review;
import es.codeurjc.holamundo.repository.BookRepository;
import es.codeurjc.holamundo.repository.ReviewRepository;
import es.codeurjc.holamundo.service.BookList;
import es.codeurjc.holamundo.component.ReviewC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookPageController {

    private BookList books;
    private int selectedBookID;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    //Constructor
    public BookPageController() {
        this.books = new BookList();
    }

    @GetMapping("/book/{bookID}")
    public String loadBookPage(Model model, @PathVariable int bookID) {
        // Get ratings from the database
        List<Double> ratings = bookRepository.getRatingsByBookId(bookID);
        System.out.println(ratings.toString());

        // Calculate the average rating
        double averageRating = 0;
        if (ratings.size() > 0) {
            for (Double rating : ratings) {
                averageRating += rating;
            }
            averageRating /= ratings.size();
        }

        // truncate averageRating to 2 decimal places
        averageRating = Math.round(averageRating * 100.0) / 100.0;

        // Get book from the database
        Book book = bookRepository.findByID(bookID);

        this.selectedBookID = bookID;

        String bookTitle = book.getTitle();
        String bookAuthor = book.getAuthorString();
        String bookDescription = book.getDescription();
        String bookImage = book.getImage();
        String bookDate = book.getReleaseDate();
        String bookISBN = book.getISBN();
        String bookGenre = book.getGenre().getName();
        String bookSeries = book.getSeries();
        int bookPageCount = book.getPageCount();
        String bookPublisher = book.getPublisher();

        model.addAttribute("id", bookID);
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
        model.addAttribute("averageRating", averageRating);

        // Get reviews from the database
        List<Review> reviews = reviewRepository.findByBookID(bookID, PageRequest.of(0, 6)).getContent();

        model.addAttribute("reviews", reviews);

        return "infoBookPage";
    }

    @GetMapping("/book/{bookID}/edit")
    public String loadModifyBookPage(Model model, @PathVariable int bookID) {
        //this.selectedBookID = bookID;

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

        model.addAttribute("id", bookID);
        model.addAttribute("Title", bookTitle);
        model.addAttribute("Author", bookAuthor);
        model.addAttribute("Description", bookDescription);
        model.addAttribute("Image", bookImage);
        model.addAttribute("Date", bookDate);
        model.addAttribute("ISBN", bookISBN);
        model.addAttribute("Genre", bookGenre);
        model.addAttribute("Serie", bookSeries);
        model.addAttribute("PageCount", bookPageCount);
        model.addAttribute("Publisher", bookPublisher);

        return "modifyBookPage";
    }

    @PostMapping("/modifyDone/{bookID}") //Parametros del formulario
    public String modifyDone(Model model, @PathVariable("bookID") int bookID, @RequestParam("inputBookName") String inputBookName
            , @RequestParam("inputBookAuthorName") String inputBookAuthorName, @RequestParam("inputBookISBN") String inputBookISBN
            , @RequestParam("inputBookPages") int inputBookPages, @RequestParam("inputBookGenre") String inputBookGenre
            , @RequestParam("inputBookDate") String inputBookDate
            , @RequestParam("inputBookPublisher") String inputBookPublisher, @RequestParam("inputBookSeries") String inputBookSeries
            , @RequestParam("inputBookDescription") String inputBookDescription) {

        //Se puede realizar con setter en la clase Book
        books.getBook(bookID).setTitle(inputBookName);
        books.getBook(bookID).setAuthor(inputBookAuthorName);
        books.getBook(bookID).setISBN(inputBookISBN);
        books.getBook(bookID).setPageCount(inputBookPages);
        books.getBook(bookID).setGenre(inputBookGenre);
        books.getBook(bookID).setRelease(inputBookDate);
        books.getBook(bookID).setPublisher(inputBookPublisher);
        books.getBook(bookID).setSeries(inputBookSeries);
        books.getBook(bookID).setDescription(inputBookDescription);

        //Tambien se puede crear una clase Book y sobreescribir con updateBook y el id
        //Book newBook = new Book(bookID, inputBookName, inputBookAuthorName, inputBookDescription, inputBookDate, inputBookDescription, inputBookISBN, inputBookGenre, inputBookSeries, inputBookPages, inputBookPublisher);
        //books.updateBook(bookID, newBook);

        return "redirect:/book/" + bookID;
    }

    @PostMapping("/book/{currentBookID}/loadMoreReviews")
    public ResponseEntity<ArrayList<Review>> loadMoreReviews(@PathVariable int currentBookID, @RequestParam int page, @RequestParam int pageSize) {
        // Get reviews from the database
        List<Review> reviews = reviewRepository.findByBookID(currentBookID, PageRequest.of(page, pageSize)).getContent();

        // COMMENTED CODE: List<ReviewC> bookReviews = books.getBook(currentBookID).getReviewsRange(page, page + pageSize);
        return new ResponseEntity<>(new ArrayList<>(reviews), HttpStatus.OK);

    }

}
