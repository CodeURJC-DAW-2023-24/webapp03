package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.*;
import es.codeurjc.webapp03.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

@RestController
public class APIBookController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookReviewService reviewService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private GenreService genreService;

    interface BookBasicView extends Book.BasicInfo {}
    //Get existing book
    @JsonView(BookBasicView.class)
    @GetMapping("api/books/{id}")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> getBook(HttpServletRequest request, @PathVariable int id){
        Book book = bookService.getBook(id);
        // Check if the book exists
        if (book == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
    }

    //Create book
    @JsonView(BookBasicView.class)
    @PostMapping("api/books")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createBook(HttpServletRequest request,
                                        @RequestParam(value = "name") String newName,
                                        @RequestParam(value = "author") String newAuthor,
                                        @RequestParam(value = "ISBN") String newISBN,
                                        @RequestParam(value = "pages") String newPages,
                                        @RequestParam(value = "genre") String newGenre,
                                        @RequestParam(value = "date") String newDate,
                                        @RequestParam(value = "publisher") String newPublisher,
                                        @RequestParam(value = "series") String newSeries,
                                        @RequestParam(value = "description") String newDescription,
                                        @RequestParam(value = "image") MultipartFile newImage){
        Principal loggedUser = request.getUserPrincipal();
        User user = userService.getUser(loggedUser.getName());
        //Check if its logged in as author or as admin
        if (user.getRole().contains("ADMIN")){
            //Book book = new Book(newName,newDescription,"",newDate,newISBN,newSeries,Integer.parseInt(newPages),newPublisher);
            Book book = new Book();
            if (insertInfoBook(newName, newAuthor, newISBN, newPages, newGenre, newDate, newPublisher, newSeries, newDescription, newImage, book) != null){
                return new ResponseEntity<>("Image not supported. Try different file", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            bookService.saveBook(book); //Save book with all changes
            return new ResponseEntity<>(book, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("You dont have permission to do this!", HttpStatus.FORBIDDEN);
        }
    }

    //@JsonView(BookBasicView.class)
    @DeleteMapping("api/books/{id}")
    public ResponseEntity<?> deleteBook(HttpServletRequest request, @PathVariable int id){
        Book book = bookService.getBook(id);
        // Check if the book exists
        if (book == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        } else {
            Principal loggedUser = request.getUserPrincipal();
            User user = userService.getUser(loggedUser.getName());
            if(user.getRole().contains("ADMIN")){
                //Delete from users lists
                for (User userBD : userService.getAllUsers()) {
                    userBD.removeReadBook(book);
                    userBD.removeReadingBook(book);
                    userBD.removeWantedBook(book);

                    userService.saveUser(userBD);
                }

                //Delete all reviews from the book
                for (Review review : reviewService.findByBook(book)) {
                    book.removeReview(review);

                    reviewService.deleteReview(review);
                }

                bookService.delete(id);
                return new ResponseEntity<>(book.getTitle()+" has been deleted", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("You dont have permission to do this!", HttpStatus.FORBIDDEN);
            }
        }
    }

    //Modify existing book
    @JsonView(BookBasicView.class)
    @PutMapping("api/books/{id}")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> editBook(HttpServletRequest request, @PathVariable int id,
                                      @RequestParam(value = "name", required = false) String newName,
                                      @RequestParam(value = "author", required = false) String newAuthor,
                                      @RequestParam(value = "ISBN", required = false) String newISBN,
                                      @RequestParam(value = "pages", required = false) String newPages,
                                      @RequestParam(value = "genre", required = false) String newGenre,
                                      @RequestParam(value = "date", required = false) String newDate,
                                      @RequestParam(value = "publisher", required = false) String newPublisher,
                                      @RequestParam(value = "series", required = false) String newSeries,
                                      @RequestParam(value = "description", required = false) String newDescription,
                                      @RequestParam(value = "image", required = false) MultipartFile newImage){
        Principal loggedUser = request.getUserPrincipal();
        User user = userService.getUser(loggedUser.getName());
        Book book = bookService.getBook(id);
        // Check if the book exists
        if(book == null){
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        //Check if its logged in as author or as admin
        if (book.getAuthor().contains(user.getUsername()) || user.getRole().contains("ADMIN")){
            if (insertInfoBook(newName, newAuthor, newISBN, newPages, newGenre, newDate, newPublisher, newSeries, newDescription, newImage, book) != null){
                return new ResponseEntity<>("Image not supported. Try different file", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            bookService.saveBook(book); //Save book with all changes
            return new ResponseEntity<>(book, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("You dont have permission to do this!", HttpStatus.FORBIDDEN);
        }
    }

    private ResponseEntity<String> insertInfoBook(String newName, String newAuthor, String newISBN, String newPages, String newGenre, String newDate, String newPublisher, String newSeries, String newDescription, MultipartFile newImage, Book book) {
        if(newName != null) book.setTitle(newName);
        if(newISBN != null) book.setISBN(newISBN);
        if(newDate != null) book.setReleaseDate(newDate);
        if(newPublisher != null) book.setPublisher(newPublisher);
        if(newSeries != null) book.setSeries(newSeries);
        if(newDescription != null) book.setDescription(newDescription);
        if(newPages != null) book.setPageCount(Integer.parseInt(newPages));

        //Check for author
        if(newAuthor != null){
            //Check if authors exist, they are separated by ","
            String[] authorsSplit = newAuthor.split(",");
            ArrayList<Author> authorList = new ArrayList<>();
            for (int i = 0; i < authorsSplit.length; i++) {
                Author found = authorService.getAuthor(authorsSplit[i]);
                if (found != null) {
                    authorList.add(found); //If found, adds the object to the list
                } else {
                    Author auxAuthor = new Author(authorsSplit[i]);//If not found, adds object to the list and service
                    auxAuthor.addBook(book); //Add author to DB
                    authorList.add(auxAuthor); //Add to list
                    authorService.saveAuthor(auxAuthor);
                }
            }
            book.setAuthor(authorList); //Add author/s to list
        }
        ;
        //Check for genre
        if(newGenre != null){
            //Check if Genre exist
            Genre genreFound = genreService.getGenre(newGenre);
            if (genreFound != null) {
                book.setGenre(genreFound);
            } else {
                Genre auxGenre = new Genre(newGenre);
                book.setGenre(auxGenre); //Add genre to Book
                auxGenre.addBook(book);
                genreService.saveGenre(auxGenre);
            }
        };
        //Should add check to only let images upload
        if(newImage != null){
            try {
                book.setImageFile(BlobProxy.generateProxy(newImage.getInputStream(), newImage.getSize()));
            }catch(IOException e){
                return new ResponseEntity<>("Image not supported. Try different file", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return null;
    }
}
