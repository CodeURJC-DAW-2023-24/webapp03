package es.codeurjc.webapp03.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.codeurjc.webapp03.entity.*;
import es.codeurjc.webapp03.security.jwt.AuthResponse;
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
    public ResponseEntity<?> getBook(HttpServletRequest request, @PathVariable int id){
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
            @ApiResponse(responseCode = "500", description = "Image not supported. Try different file", content = @Content) //Internal server error
    })
    //Create book
    @JsonView(BookBasicView.class)
    @PostMapping("/api/books")
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
                                        @RequestParam(value = "image", required = false) MultipartFile newImage) throws IOException {
        Principal loggedUser = request.getUserPrincipal();
        User user = userService.getUser(loggedUser.getName());
        //Check if it's logged in as admin
        if(user == null){
            return new ResponseEntity<>("You must log in as admin!", HttpStatus.UNAUTHORIZED);
        }
        if (user.getRole().contains("ADMIN")){
            if (newName == null || newName.isBlank()) return new ResponseEntity<>("Book name can't be blank!", HttpStatus.CONFLICT); //Name cant be blank
            if (newAuthor == null || newAuthor.isBlank()) return new ResponseEntity<>("Author can't be blank!", HttpStatus.CONFLICT); //Author cant be blank
            if (newISBN == null || newISBN.isBlank()) return new ResponseEntity<>("ISBN can't be blank!", HttpStatus.CONFLICT); //ISBN cant be blank

            Book book = new Book();
            insertInfoBook(newName, newAuthor, newISBN, newPages, newGenre, newDate, newPublisher, newSeries, newDescription, book);
            if(newImage != null){ //If image is added...
                //Check if image file is correct
                try (InputStream input = newImage.getInputStream()) {
                    try {
                        ImageIO.read(input).toString(); //If it doesn't fail, this is an image.
                        book.setImageFile(BlobProxy.generateProxy(newImage.getInputStream(), newImage.getSize()));
                    } catch (Exception e) {
                        // It's not an image.
                        return new ResponseEntity<>("Image not supported. Try different file", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }else{
                //Add default image
                book.setImageFile(book.URLtoBlob("https://fissac.com/wp-content/uploads/2020/11/placeholder.png"));
            }
            bookService.saveBook(book); //Save book with all changes
            return new ResponseEntity<>(book, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("You dont have permission to do this!", HttpStatus.UNAUTHORIZED);
        }
    };

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
                                      @RequestParam(value = "name", required = false) String newName,
                                      @RequestParam(value = "author", required = false) String newAuthor,
                                      @RequestParam(value = "ISBN", required = false) String newISBN,
                                      @RequestParam(value = "pages", required = false) String newPages,
                                      @RequestParam(value = "genre", required = false) String newGenre,
                                      @RequestParam(value = "date", required = false) String newDate,
                                      @RequestParam(value = "publisher", required = false) String newPublisher,
                                      @RequestParam(value = "series", required = false) String newSeries,
                                      @RequestParam(value = "description", required = false) String newDescription,
                                      @RequestParam(value = "image", required = false) MultipartFile newImage) throws IOException {
        Principal loggedUser = request.getUserPrincipal();
        User user = userService.getUser(loggedUser.getName());
        Book book = bookService.getBook(id);
        if(user == null){
            return new ResponseEntity<>("You must log in as admin or author!", HttpStatus.UNAUTHORIZED);
        }
        // Check if the book exists
        if(book == null){
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        //Check if it's logged in as author or as admin
        if (user.getRole().contains("ADMIN") || authorService.checkCorrectAuthor(user.getUsername(),book.getAuthor())){
            if (newName == null || newName.isBlank()) return new ResponseEntity<>("Book name can't be blank!", HttpStatus.CONFLICT); //Name cant be blank
            if (newAuthor == null || newAuthor.isBlank()) return new ResponseEntity<>("Author can't be blank!", HttpStatus.CONFLICT); //Author cant be blank
            if (newISBN == null || newISBN.isBlank()) return new ResponseEntity<>("ISBN can't be blank!", HttpStatus.CONFLICT); //ISBN cant be blank
            insertInfoBook(newName, newAuthor, newISBN, newPages, newGenre, newDate, newPublisher, newSeries, newDescription, book);

            //If no image is added, keep original.
            if(newImage != null){
                //Check if image file is correct
                try (InputStream input = newImage.getInputStream()) {
                    try {
                        ImageIO.read(input).toString(); //If it doesn't fail, this is an image.
                        book.setImageFile(BlobProxy.generateProxy(newImage.getInputStream(), newImage.getSize()));
                    } catch (Exception e) {
                        // It's not an image.
                        return new ResponseEntity<>("Image not supported. Try different file", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }

            bookService.saveBook(book); //Save book with all changes
            return new ResponseEntity<>(book, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("You dont have permission to do this!", HttpStatus.UNAUTHORIZED);
        }
    }

    // Get book's image by ID from the database
    @Operation(summary = "Get book image by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book image correctly retrieved", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content) //Not found
    })
    @GetMapping("/api/books/{id}/image")
    public ResponseEntity<Object> getBookImage(HttpServletRequest request, @PathVariable int id) throws SQLException {
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

    private void insertInfoBook(String newName, String newAuthor, String newISBN, String newPages, String newGenre, String newDate, String newPublisher, String newSeries, String newDescription, Book book) throws IOException {
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
        };

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
        };
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        return new ResponseEntity<>(name + " parameter is missing", HttpStatus.BAD_REQUEST);
    }
}
