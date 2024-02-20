package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.service.BookList;
import es.codeurjc.holamundo.component.ReviewC;
import es.codeurjc.holamundo.entity.Book;
import es.codeurjc.holamundo.repository.BookRepository;

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

    @Autowired
    private BookRepository booksBD;

    @Autowired
    private ReviewRepository reviewRepository;

    private Book bookInfo;
    private BookList books;
    private int selectedBookID;

    // Constructor
    public BookPageController() {
        this.books = new BookList();
    }

    // Get books from "BookRepository.java"
    @GetMapping(value = { "/book/{bookID}", "/book/{bookTitle}" })
    public Book loadBookPage(@PathVariable(value = "bookID") Long ID,
            @PathVariable(value = "bookTitle", required = false) String title) {
        // Get book from the database
        this.bookInfo = posts.findById(id).orElseThrow();

        // Get ratings from the database
        List<Double> ratings = booksBD.getRatingsByBookId(bookID);
        System.out.println(ratings.toString());

        // Calculate the average rating
        double averageRating = 0;
        if (ratings.size() > 0) {
            for (Double rating : ratings) {
                averageRating += rating;
            }
            averageRating /= ratings.size();
        }

        averageRating = Math.round(averageRating * 100.0) / 100.0;

        model.addAttribute("id", bookInfo.getID());
        model.addAttribute("bookTitle", bookInfo.getTitle());
        model.addAttribute("bookAuthor", bookInfo.getAuthor());
        model.addAttribute("bookDescription", bookInfo.getDescription());
        model.addAttribute("bookImage", bookInfo.getImage);
        model.addAttribute("bookDate", bookInfo.getReleaseDate());
        model.addAttribute("bookISBN", bookInfo.getISBN);
        model.addAttribute("bookGenre", bookInfo.getGenre());
        model.addAttribute("bookSeries", bookInfo.getSeries);
        model.addAttribute("bookPageCount", bookInfo.getPageCount());
        model.addAttribute("bookPublisher", bookInfo.getPublisher());

        // Get reviews from the database
        List<Review> reviews = reviewRepository.findByBookID(bookID, PageRequest.of(0, 6)).getContent();

        model.addAttribute("reviews", reviews);

        return "infoBookPage";
    }

    @GetMapping("/book/{bookID}/edit")
    public String loadModifyBookPage(Model model, @PathVariable int bookID) {
        model.addAttribute("id", newInfo.getID());
        model.addAttribute("bookTitle", newInfo.getTitle());
        model.addAttribute("bookAuthor", newInfo.getAuthor());
        model.addAttribute("bookDescription", newInfo.getDescription());
        model.addAttribute("bookImage", newInfo.getImage);
        model.addAttribute("bookDate", newInfo.getReleaseDate());
        model.addAttribute("bookISBN", newInfo.getISBN);
        model.addAttribute("bookGenre", newInfo.getGenre());
        model.addAttribute("bookSeries", newInfo.getSeries);
        model.addAttribute("bookPageCount", newInfo.getPageCount());
        model.addAttribute("bookPublisher", newInfo.getPublisher());

        return "modifyBookPage";
    }

    @PostMapping("/modifyDone/{bookID}") // Parametros del formulario
    public String modifyDone(Model model, @PathVariable("bookID") int bookID,
            @RequestParam("inputBookName") String inputBookName,
            @RequestParam("inputBookAuthorName") String inputBookAuthorName,
            @RequestParam("inputBookISBN") String inputBookISBN, @RequestParam("inputBookPages") int inputBookPages,
            @RequestParam("inputBookGenre") String inputBookGenre, @RequestParam("inputBookDate") String inputBookDate,
            @RequestParam("inputBookPublisher") String inputBookPublisher,
            @RequestParam("inputBookSeries") String inputBookSeries,
            @RequestParam("inputBookDescription") String inputBookDescription) {

        // Se puede realizar con setter en la clase Book
        books.getBook(bookID).setTitle(inputBookName);
        books.getBook(bookID).setAuthor(inputBookAuthorName);
        books.getBook(bookID).setISBN(inputBookISBN);
        books.getBook(bookID).setPageCount(inputBookPages);
        books.getBook(bookID).setGenre(inputBookGenre);
        books.getBook(bookID).setRelease(inputBookDate);
        books.getBook(bookID).setPublisher(inputBookPublisher);
        books.getBook(bookID).setSeries(inputBookSeries);
        books.getBook(bookID).setDescription(inputBookDescription);

        // Tambien se puede crear una clase Book y sobreescribir con updateBook y el id
        // Book newBook = new Book(bookID, inputBookName, inputBookAuthorName,
        // inputBookDescription, inputBookDate, inputBookDescription, inputBookISBN,
        // inputBookGenre, inputBookSeries, inputBookPages, inputBookPublisher);
        // books.updateBook(bookID, newBook);

        booksBD.replace(books.getBook(bookID));

        return "redirect:/book/" + bookID;
    }

    @PostMapping("/book/{currentBookID}/loadMoreReviews")
    public ResponseEntity<ArrayList<ReviewC>> loadMoreReviews(@PathVariable int currentBookID, @RequestParam int page,
            @RequestParam int pageSize) {
        // Get reviews from the database
        List<Review> reviews = reviewRepository.findByBookID(currentBookID, PageRequest.of(page, pageSize));

        // COMMENTED CODE: List<ReviewC> bookReviews =
        // books.getBook(currentBookID).getReviewsRange(page, page + pageSize);
        return new ResponseEntity<>(new ArrayList<>(reviews), HttpStatus.OK);
    }

}
