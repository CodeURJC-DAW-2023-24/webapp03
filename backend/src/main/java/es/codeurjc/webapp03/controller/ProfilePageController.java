package es.codeurjc.webapp03.controller;

import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.service.BookService;
import es.codeurjc.webapp03.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ProfilePageController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @GetMapping("/profile/{username}/**")
    public String loadProfilePage(Model model, @PathVariable String username, HttpServletRequest request) throws SQLException {

        // Get user from the database
        User user = userService.getUser(username);
        user.setProfileImageString(user.blobToString(user.getProfileImageFile()));
        user.setProfileImageString(user.blobToString(user.getProfileImageFile()));
        //User info
        List<String> userRoles = user.getRole();

        // Search for admin role or Author role
        String role = "USER";
        if (userRoles.contains("ADMIN")) {
            role = "ADMIN";
        } else if (userRoles.contains("AUTHOR")) {
            role = "AUTHOR";
        }

        boolean isCurrentUser = false;
        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            model.addAttribute("user", true);
            String currentUsername = authentication.getName();
            model.addAttribute("currentUsername", currentUsername);
            model.addAttribute("currentUserRole", userRoles.contains("AUTHOR"));
            if (currentUsername.equals(username)) {
                isCurrentUser = true;
            } else {
                isCurrentUser = false;
            }
        }
        model.addAttribute("currentUser", isCurrentUser);

        String alias = user.getAlias();
        String description = user.getDescription();
        String profileImage = user.getProfileImageString();
        String email = user.getEmail();
        String password = user.getPassword();

        model.addAttribute("username", username);
        model.addAttribute("alias", alias);
        model.addAttribute("role", role);
        model.addAttribute("description", description);
        model.addAttribute("profileImageString", profileImage);
        model.addAttribute("email", email);
        model.addAttribute("password", password);

        int nReadBooks = user.getReadBooks().size();
        int nReadingBooks = user.getReadingBooks().size();
        int nWantedBooks = user.getWantedBooks().size();
        int nReviews = user.getReviews().size();

        List<Book> readBooksList = userService.getListPageable(user, "read", PageRequest.of(0, 4));
        List<Book> readingBooksList = userService.getListPageable(user, "reading", PageRequest.of(0, 4));
        List<Book> wantedBooksList = userService.getListPageable(user, "wanted", PageRequest.of(0, 4));

        for (int i = 0; i < readBooksList.size(); i++) {
            readBooksList.get(i).setImageString(readBooksList.get(i).blobToString(readBooksList.get(i).getImageFile()));
        }

        for (int i = 0; i < readingBooksList.size(); i++) {
            readingBooksList.get(i).setImageString(readingBooksList.get(i).blobToString(readingBooksList.get(i).getImageFile()));
        }

        for (int i = 0; i < wantedBooksList.size(); i++) {
            wantedBooksList.get(i).setImageString(wantedBooksList.get(i).blobToString(wantedBooksList.get(i).getImageFile()));
        }

        List<Double> readBooksRatings = new ArrayList<>();
        readBooksList.forEach((book) -> {
            List<Double> bookRatings = bookService.getRatings(book.getID());
            double averageRating = 0;
            if (bookRatings.size() > 0) {
                for (Double rating : bookRatings) {
                    averageRating += rating;
                }
                averageRating /= bookRatings.size();
            }
            readBooksRatings.add(averageRating);
        });
        model.addAttribute("ratingsRead", readBooksRatings);

        List<Double> readingBooksRatings = new ArrayList<>();
        readingBooksList.forEach((book) -> {
            List<Double> bookRatings = bookService.getRatings(book.getID());
            double averageRating = 0;
            if (bookRatings.size() > 0) {
                for (Double rating : bookRatings) {
                    averageRating += rating;
                }
                averageRating /= bookRatings.size();
            }
            readingBooksRatings.add(averageRating);
        });
        model.addAttribute("ratingsReading", readingBooksRatings);

        List<Double> wantedBooksRatings = new ArrayList<>();
        wantedBooksList.forEach((book) -> {
            List<Double> bookRatings = bookService.getRatings(book.getID());
            double averageRating = 0;
            if (bookRatings.size() > 0) {
                for (Double rating : bookRatings) {
                    averageRating += rating;
                }
                averageRating /= bookRatings.size();
            }
            wantedBooksRatings.add(averageRating);
        });
        model.addAttribute("ratingsWanted", wantedBooksRatings);

        model.addAttribute("nReadBooks", nReadBooks);
        model.addAttribute("nReadingBooks", nReadingBooks);
        model.addAttribute("nWantedBooks", nWantedBooks);
        model.addAttribute("nReviews", nReviews);
        model.addAttribute("ReadBooks", readBooksList);
        model.addAttribute("ReadingBooks", readingBooksList);
        model.addAttribute("WantedBooks", wantedBooksList);

        //Admin
        model.addAttribute("admin", request.isUserInRole("ADMIN"));

        //Unregistered user
        model.addAttribute("noUser", !request.isUserInRole("USER"));


        return "profilePage";
    }

    @GetMapping("/profile/{username}/loadMore")
    public String loadReadBooks(@PathVariable String username, @RequestParam(defaultValue = "default") String listType, @RequestParam int page, @RequestParam int size, Model model) throws SQLException {
        switch (listType) {
            case "read" -> {
                List<Book> readBooksList = userService.getListPageable(userService.getUser(username), "read", PageRequest.of(page, size));

                for (int i = 0; i < readBooksList.size(); i++) {
                    readBooksList.get(i).setImageString(readBooksList.get(i).blobToString(readBooksList.get(i).getImageFile()));
                }

                model.addAttribute("bookItem", readBooksList);

                List<Double> readBooksRatings = new ArrayList<>();
                readBooksList.forEach((book) -> {
                    List<Double> bookRatings = bookService.getRatings(book.getID());
                    double averageRating = 0;
                    if (bookRatings.size() > 0) {
                        for (Double rating : bookRatings) {
                            averageRating += rating;
                        }
                        averageRating /= bookRatings.size();
                    }
                    readBooksRatings.add(averageRating);
                });
                model.addAttribute("ratings", readBooksRatings);
            }
            case "reading" -> {
                List<Book> readingBooksList = userService.getListPageable(userService.getUser(username), "reading", PageRequest.of(page, size));

                for (int i = 0; i < readingBooksList.size(); i++) {
                    readingBooksList.get(i).setImageString(readingBooksList.get(i).blobToString(readingBooksList.get(i).getImageFile()));
                }

                model.addAttribute("bookItem", readingBooksList);

                List<Double> readingBooksRatings = new ArrayList<>();
                readingBooksList.forEach((book) -> {
                    List<Double> bookRatings = bookService.getRatings(book.getID());
                    double averageRating = 0;
                    if (bookRatings.size() > 0) {
                        for (Double rating : bookRatings) {
                            averageRating += rating;
                        }
                        averageRating /= bookRatings.size();
                    }
                    readingBooksRatings.add(averageRating);
                });
                model.addAttribute("ratings", readingBooksRatings);
            }
            case "wanted" -> {
                List<Book> wantedBooksList = userService.getListPageable(userService.getUser(username), "wanted", PageRequest.of(page, size));

                for (int i = 0; i < wantedBooksList.size(); i++) {
                    wantedBooksList.get(i).setImageString(wantedBooksList.get(i).blobToString(wantedBooksList.get(i).getImageFile()));
                }

                model.addAttribute("bookItem", wantedBooksList);

                List<Double> wantedBooksRatings = new ArrayList<>();
                wantedBooksList.forEach((book) -> {
                    List<Double> bookRatings = bookService.getRatings(book.getID());
                    double averageRating = 0;
                    if (bookRatings.size() > 0) {
                        for (Double rating : bookRatings) {
                            averageRating += rating;
                        }
                        averageRating /= bookRatings.size();
                    }
                    wantedBooksRatings.add(averageRating);
                });
                model.addAttribute("ratings", wantedBooksRatings);
            }
        }

        return "bookListsItemTemplate";


    }

    @GetMapping("/profile/{username}/delete")
    public String deleteUser(Model model, @PathVariable String username, HttpServletRequest request) throws SQLException {
        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            String currentUsername = authentication.getName();
            User user = userService.getUser(currentUsername);
            if (user.getRole().contains("ADMIN")) {
                userService.deleteUser(userService.getUser(username));
            } else {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }
        return "redirect:/admin";
    }

    @GetMapping("/profile/{username}/convertAuthor")
    public String getMethodName(@PathVariable String username, HttpServletRequest request) {
        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            User user = userService.getUser(authentication.getName());
            if (user.getRole().contains("ADMIN")) {
                userService.makeAuthor(userService.getUser(username));
            } else {
                return "redirect:/error";
            }
        } else {
            return "redirect:/error";
        }
        return "redirect:/profile/{username}";
    }

    @GetMapping("/profile/{username}/removeAuthor")
    public String removeAuthorRol(@PathVariable String username, HttpServletRequest request) {
        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            User user = userService.getUser(authentication.getName());
            if (user.getRole().contains("ADMIN")) {
                userService.removeAuthor(userService.getUser(username));
            } else {
                return "redirect:/error";
            }
        } else {
            return "redirect:/error";
        }
        return "redirect:/profile/{username}";
    }



    @GetMapping("/profile/{username}/exportLists")
    public ResponseEntity<String> exportLists(@PathVariable String username, HttpServletRequest request) throws IOException {
        String allBooksCSV = userService.exportUserListsToCSV(username);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + username + "Lists.csv\"")
                .body(allBooksCSV);
    }

}