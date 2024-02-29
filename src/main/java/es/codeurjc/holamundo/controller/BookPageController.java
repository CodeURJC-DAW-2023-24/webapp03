package es.codeurjc.holamundo.controller;

import es.codeurjc.holamundo.entity.*;
import es.codeurjc.holamundo.repository.BookRepository;
import es.codeurjc.holamundo.repository.ReviewRepository;
import es.codeurjc.holamundo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;

import es.codeurjc.holamundo.repository.AuthorRepository;
import es.codeurjc.holamundo.repository.GenreRepository;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class BookPageController {

    Book book;
    private String currentUsername;
    private boolean isUser;
    private boolean isAdmin;


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorRepository authorsBD;

    @Autowired
    private GenreRepository genreBD;

    //delete?? not being used
    //Constructor
    public BookPageController() {
        //this.books = new BookList();
    }

    @GetMapping("/book/{bookID}")
    public String loadBookPage(Model model, @PathVariable int bookID, HttpServletRequest request) throws SQLException {

        //Get current user
        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            currentUsername = authentication.getName();

            User user = userRepository.findByUsername(currentUsername);
            user.setProfileImageString(user.blobToString(user.getProfileImageFile()));
            model.addAttribute("profileImageString", user.getProfileImageString());
            model.addAttribute("username", currentUsername);

            isUser = true;
            isAdmin = request.isUserInRole("ADMIN");

            String authorUser = bookRepository.findByID(bookID).getAuthorString();

            if(currentUsername.equals(authorUser) && user.getRole().contains("AUTHOR")){
                model.addAttribute("author",true);
            }
        } else {
            isUser = false;
        }

        // Get ratings from the database
        List<Double> ratings = bookRepository.getRatingsByBookId(bookID);

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
        this.book = bookRepository.findByID(bookID);

        String bookTitle = book.getTitle();
        String bookAuthor = book.getAuthorString();
        String bookDescription = book.getDescription();
        //String bookImage = book.getImage();
        String bookDate = book.getReleaseDate();
        String bookISBN = book.getISBN();
        String bookGenre = book.getGenre().getName();
        String bookSeries = book.getSeries();
        int bookPageCount = book.getPageCount();
        String bookPublisher = book.getPublisher();

        //Convert the imageFile to Base64 for it to appear in html
        Blob blob = book.getImageFile();
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        String bookImage = Base64.getEncoder().encodeToString(bytes);

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

        if (isUser == true) {
            model.addAttribute("user", true);
            if (isAdmin == true) {
                model.addAttribute("admin", true);
            } else {
                model.addAttribute("admin", false);
            }
            // Check if the user has already reviewed the book
            boolean hasReviewed = reviewRepository.existsByBookIDAndAuthorUsername(bookID, currentUsername);

            // Get the user's review
            Review userReview = reviewRepository.findByBookIDAndAuthorUsername(bookID, currentUsername);
            model.addAttribute("hasReviewed", hasReviewed);
            model.addAttribute("userReview", userReview);

        } else {
            model.addAttribute("user", false);
        }

        model.addAttribute("reviews", reviews);

        //Authors and admins
        model.addAttribute("modifyButton", request.isUserInRole("AUTHOR"));
        model.addAttribute("modifyButton", request.isUserInRole("ADMIN"));
        //Admin
        model.addAttribute("admin", request.isUserInRole("ADMIN"));

        // check if the book is in the user's read, reading or wanted books (only if the user is logged in)
        if(isUser){
            // check if the book is in the user's read, reading or wanted books (only if the user is logged in)
            boolean isRead = userRepository.findByUsername(currentUsername).getReadBooks().contains(book);
            boolean isReading = userRepository.findByUsername(currentUsername).getReadingBooks().contains(book);
            boolean isWanted = userRepository.findByUsername(currentUsername).getWantedBooks().contains(book);

            if (isRead) {
                model.addAttribute("bookState", "read");
            } else if (isReading) {
                model.addAttribute("bookState", "reading");
            } else if (isWanted) {
                model.addAttribute("bookState", "wanted");
            } else {
                model.addAttribute("bookState", "none");
            }
        }

        return "infoBookPage";
    }

    @GetMapping("/book/{bookID}/edit")
    public String loadModifyBookPage(Model model, @PathVariable int bookID, HttpServletRequest request) throws SQLException {

        model.addAttribute("username", currentUsername);
        // Check if the user is an author or an admin
        if (!request.isUserInRole("AUTHOR") && !request.isUserInRole("ADMIN")) {
            return "redirect:/book/" + bookID;
        } else { // if it's an author and the book is not his, redirect to the book page
            if (request.isUserInRole("AUTHOR")) {

                if (!bookRepository.findByID(bookID).getAuthor().contains(authorsBD.findByName(userRepository.findByUsername(currentUsername).getAlias()))) {
                    return "redirect:/book/" + bookID;
                }
            }
        }

         // Get the current logged in user
         Authentication authentication = (Authentication) request.getUserPrincipal();
             String currentUsername = authentication.getName();
             User user = userRepository.findByUsername(currentUsername);
             String imageString = user.blobToString(user.getProfileImageFile());
             model.addAttribute("profileImageString", imageString);

        String bookTitle = book.getTitle();
        String bookAuthor = book.getAuthorString();
        String bookDescription = book.getDescription();
        //String bookImage = book.getImage();
        String bookDate = book.getReleaseDate();
        String bookISBN = book.getISBN();
        Genre bookGenre = book.getGenre();
        String bookSeries = book.getSeries();
        int bookPageCount = book.getPageCount();
        String bookPublisher = book.getPublisher();

        //Convert the imageFile to Base64 for it to appear in html
        Blob blob = book.getImageFile();
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        String bookImage = Base64.getEncoder().encodeToString(bytes);

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

        //Admin
        //model.addAttribute("admin", request.isUserInRole("ADMIN"));

        return "modifyBookPage";
    }

    @PostMapping("/modifyDone/{bookID}") //Parametros del formulario
    public String modifyDone(Model model,HttpServletRequest request, @PathVariable("bookID") int bookID, @RequestParam("inputBookName") String inputBookName
            , @RequestParam("inputBookAuthorName") String inputBookAuthorName, @RequestParam("inputBookISBN") String inputBookISBN
            , @RequestParam("inputBookPages") int inputBookPages, @RequestParam("inputBookGenre") String inputBookGenre
            , @RequestParam("inputBookDate") String inputBookDate
            , @RequestParam("inputBookPublisher") String inputBookPublisher, @RequestParam("inputBookSeries") String inputBookSeries
            , @RequestParam("inputBookDescription") String inputBookDescription, @RequestParam("inputImageFile") MultipartFile InputImageFile
            , @RequestParam String accion) throws IOException, SQLException {

        Authentication authentication = (Authentication) request.getUserPrincipal();
        String currentUsername = authentication.getName();
        User user = userRepository.findByUsername(currentUsername);
        String authorUser = bookRepository.findByID(bookID).getAuthorString();

        if((user.getUsername().equals(authorUser) && user.getRole().contains("AUTHOR")) || user.getRole().contains("ADMIN")){
            Book book = bookRepository.findByID(bookID);
            if ("Guardar".equals(accion)) {
                //Se puede realizar con setter en la clase Book
                book.setTitle(inputBookName);
                book.setISBN(inputBookISBN);
                book.setPageCount(inputBookPages);
                book.setReleaseDate(inputBookDate);
                book.setPublisher(inputBookPublisher);
                book.setSeries(inputBookSeries);
                book.setDescription(inputBookDescription);

                //Check if authors exist, they are separated by ","
                String[] authorsSplit = inputBookAuthorName.split(",");
                ArrayList<Author> authorList = new ArrayList<>();
                for (int i = 0; i < authorsSplit.length; i++) {
                    Author found = authorsBD.findByName(authorsSplit[i]);
                    if (found != null) {
                        authorList.add(found);
                    } else {
                        Author newAuthor = new Author(authorsSplit[i]);
                        newAuthor.addBook(book); //Add author to DB
                        authorList.add(newAuthor); //Add to list
                        authorsBD.save(newAuthor);
                    }
                }
                book.setAuthor(authorList); //Add author/s to list

                //Check if Genre exist
                Genre genreFound = genreBD.findByName(inputBookGenre);
                if (genreFound != null) {
                    book.setGenre(genreFound);
                } else {
                    Genre newGenre = new Genre(inputBookGenre);
                    book.setGenre(newGenre); //Add genre to Book
                    newGenre.addBook(book);
                    genreBD.save(newGenre);
                }

                //If no picture was added, check for old pic. If there never was a pic, insert placeholder.
                if (InputImageFile.isEmpty()) {
                    if (book.getImageFile().length() == 0 || book.getImageFile() == null) {
                        book.setImageFile(book.URLtoBlob("https://fissac.com/wp-content/uploads/2020/11/placeholder.png"));
                    }
                } else {
                    book.setImageFile(BlobProxy.generateProxy(InputImageFile.getInputStream(), InputImageFile.getSize()));
                }
                bookRepository.save(book);

            }else if ("Borrar".equals(accion)) {
                bookRepository.deleteById(book.getID());
            }

            return "redirect:/book/" + bookID;
        }else{
            return"redirect:/error";
        }
    }

    @GetMapping("/book/{currentBookID}/loadMoreReviews")
    public String loadMoreReviews(@PathVariable int currentBookID, @RequestParam int page, @RequestParam int pageSize, Model model) {
        // Get reviews from the database
        List<Review> reviews = reviewRepository.findByBookID(currentBookID, PageRequest.of(page, pageSize)).getContent();

        if (isUser == true) {
            if (isAdmin == true) {
                model.addAttribute("admin", true);
            } else {
                model.addAttribute("admin", false);
            }
        }

        model.addAttribute("reviewCard", reviews);
        model.addAttribute("bookID", currentBookID);


        return "reviewItemTemplate";

    }

}
