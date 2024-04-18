package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.*;
import es.codeurjc.webapp03.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
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

    @Operation(summary = "Get book info by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book info correctly retrieved", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))
            }),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content) //Not found
    })
    //Get existing book
    @JsonView(BookBasicView.class)
    @GetMapping("/api/books/{id}")
    public ResponseEntity<?> getBook(@PathVariable int id){
        Book book = bookService.getBook(id);
        // Check if the book exists
        if (book == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
    }

    @Operation(summary = "Create new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created correctly", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))
            }),
            @ApiResponse(responseCode = "400", description = "Parameter missing.", content = @Content), //Bad request
            @ApiResponse(responseCode = "401", description = "You don't have permission to do this!", content = @Content), //Unauthorized
            @ApiResponse(responseCode = "409", description = "Invalid parameter.", content = @Content), //Conflict
    })
    //Create book
    @JsonView(BookBasicView.class)
    @PostMapping("/api/books")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createBook(HttpServletRequest request,
                                        @RequestBody Book book) throws IOException {
        Principal loggedUser = request.getUserPrincipal();
        User user = userService.getUser(loggedUser.getName());
        //Check if it's logged in as admin
        if(user == null){
            return new ResponseEntity<>("You must log in as admin!", HttpStatus.UNAUTHORIZED);
        }
        if (user.getRole().contains("ADMIN")){
            if (book.getTitle() == null || book.getTitle().isBlank()) return new ResponseEntity<>("Book name can't be blank!", HttpStatus.CONFLICT); //Name cant be blank
            if (book.getAuthor() == null || book.getAuthor().toString().isEmpty()) return new ResponseEntity<>("Author can't be blank!", HttpStatus.CONFLICT); //Author cant be blank
            if (book.getISBN() == null || book.getISBN().isBlank()) return new ResponseEntity<>("ISBN can't be blank!", HttpStatus.CONFLICT); //ISBN cant be blank
            //Book newBook = new Book();
            insertInfoBook(book.getTitle(), book.getAuthorString(), book.getISBN(), String.valueOf(book.getPageCount()), book.getGenre().toString(), book.getReleaseDate(), book.getPublisher(), book.getSeries(), book.getDescription(), book);
            //Add default image
            book.setImageFile(book.URLtoBlob("https://fissac.com/wp-content/uploads/2020/11/placeholder.png"));
            bookService.saveBook(book); //Save book
            return new ResponseEntity<>(book, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("You dont have permission to do this!", HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Delete book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted correctly", content = @Content),
            @ApiResponse(responseCode = "401", description = "You don't have permission to do this!", content = @Content), //Unauthorized
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content) //Internal server error

    })
    //Delete book
    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<?> deleteBook(HttpServletRequest request, @PathVariable int id){
        Book book = bookService.getBook(id);
        // Check if the book exists
        if (book == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        } else {
            Principal loggedUser = request.getUserPrincipal();
            User user = userService.getUser(loggedUser.getName());
            if(user == null){
                return new ResponseEntity<>("You must log in as admin!", HttpStatus.UNAUTHORIZED);
            }
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
                return new ResponseEntity<>("You dont have permission to do this!", HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @Operation(summary = "Modify book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book modified correctly", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))
            }),
            @ApiResponse(responseCode = "401", description = "You don't have permission to do this!", content = @Content), //Unauthorized
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content), //Not Found
            @ApiResponse(responseCode = "409", description = "Invalid parameter", content = @Content), //Conflict
            @ApiResponse(responseCode = "500", description = "Image not supported. Try different file", content = @Content) //Internal server error

    })
    //Modify existing book
    @JsonView(BookBasicView.class)
    @PutMapping("/api/books/{id}")
    public ResponseEntity<?> editBook(HttpServletRequest request, @PathVariable int id,
                                      @RequestBody Book book) throws IOException {
        Principal loggedUser = request.getUserPrincipal();
        User user = userService.getUser(loggedUser.getName());
        Book bookFound = bookService.getBook(id);
        if(user == null){
            return new ResponseEntity<>("You must log in as admin or author!", HttpStatus.UNAUTHORIZED);
        }
        // Check if the book exists
        if(bookFound == null){
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        //Check if it's logged in as author or as admin
        if (user.getRole().contains("ADMIN") || authorService.checkCorrectAuthor(user.getUsername(),bookFound.getAuthor())){
            if (book.getTitle() == null || book.getTitle().isBlank()) return new ResponseEntity<>("Book name can't be blank!", HttpStatus.CONFLICT); //Name cant be blank
            if (book.getAuthor() == null || book.getAuthorString().isBlank()) return new ResponseEntity<>("Author can't be blank!", HttpStatus.CONFLICT); //Author cant be blank
            if (book.getISBN() == null || book.getISBN().isBlank()) return new ResponseEntity<>("ISBN can't be blank!", HttpStatus.CONFLICT); //ISBN cant be blank
            insertInfoBook(book.getTitle(), book.getAuthorString(), book.getISBN(), String.valueOf(book.getPageCount()), book.getGenre().toString(), book.getReleaseDate(), book.getPublisher(), book.getSeries(), book.getDescription(), bookFound);
            bookService.saveBook(bookFound); //Save book with all changes
            return new ResponseEntity<>(book, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("You dont have permission to do this!", HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/api/books/{id}/image")
    public ResponseEntity<Object> updateBookImage(HttpServletRequest request, @PathVariable int id, @RequestParam MultipartFile image) throws IOException {
        Book book = bookService.getBook(id);
        Principal loggedUser = request.getUserPrincipal();
        User user = userService.getUser(loggedUser.getName());
        // Check if the book exists
        if (book == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        if (!user.getRole().contains("ADMIN") && !book.getAuthorString().equals(user.getUsername())){
            return new ResponseEntity<>("You don't have permission to do this!", HttpStatus.UNAUTHORIZED);
        }
        //If no image is added, keep original.
        if(image != null) {
            //Check if image file is correct
            try (InputStream input = image.getInputStream()) {
                try {
                    ImageIO.read(input).toString(); //If it doesn't fail, this is an image.
                    book.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.getSize()));
                    bookService.saveBook(book);
                    return new ResponseEntity<>("Image uploaded successfully.", HttpStatus.OK);
                } catch (Exception e) {
                    // It's not an image.
                    return new ResponseEntity<>("Image not supported. Try different file", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }else{
            return new ResponseEntity<>("File could not be uploaded. Try again", HttpStatus.BAD_REQUEST);
        }
    }

    // Get book's image by ID from the database
    @Operation(summary = "Get book image by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book image correctly retrieved", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content) //Not found
    })
    @GetMapping("/api/books/{id}/image")
    public ResponseEntity<Object> getBookImage(@PathVariable int id) throws SQLException {
        Book book = bookService.getBook(id);
        // Check if the book exists
        if (book == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        } else {
            Blob imageBlob = book.getImageFile();
            int blobLength = (int) imageBlob.length();
            byte[] imageBytes = imageBlob.getBytes(1, blobLength);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);

        }
    }

    private void insertInfoBook(String newName, String newAuthor, String newISBN, String newPages, String newGenre, String newDate, String newPublisher, String newSeries, String newDescription, Book book){
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
            if (newAuthor.isBlank()){
                Author auxAuthor = new Author("Not available");
                authorList.add(auxAuthor);
            }else{
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
            }
            book.setAuthor(authorList); //Add author/s to list
        }
        //Check for genre
        if(newGenre != null){
            //Check if Genre exist
            if(newGenre.isBlank()){
                Genre auxGenre = new Genre("Not available");
                book.setGenre(auxGenre);
            }else {
                Genre genreFound = genreService.getGenre(newGenre);
                if (genreFound != null) {
                    book.setGenre(genreFound);
                } else {
                    Genre auxGenre = new Genre(newGenre);
                    book.setGenre(auxGenre); //Add genre to Book
                    auxGenre.addBook(book);
                    genreService.saveGenre(auxGenre);
                }
            }
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        return new ResponseEntity<>(name + " parameter is missing", HttpStatus.BAD_REQUEST);
    }
}
