package es.codeurjc.webapp03.service;

import es.codeurjc.webapp03.entity.Author;
import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.entity.Genre;
import es.codeurjc.webapp03.entity.User;
import es.codeurjc.webapp03.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService() {

    }

    public boolean checkLoggedIn(HttpServletRequest request) {
        Authentication authentication = (Authentication) request.getUserPrincipal();
        return authentication != null;
    }

    public boolean checkCorrectProfile(String username, HttpServletRequest request) {
        Authentication authentication = (Authentication) request.getUserPrincipal();
        User user = userRepository.findByUsername(authentication.getName());
        if (authentication != null && user != null) {
            if (!user.getUsername().equals(username) && !user.getRole().contains("ADMIN")) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        userRepository.save(user);
        return user;
    }

    public void deleteUser(User user) {
        if (user != null) {
            userRepository.delete(user);
        }
    }

    public ResponseEntity<String> changePassword(User user, String newPassword, String currentPassword) throws MessagingException, IOException {
        // Compara la contraseña actual con la almacenada en la base de datos
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return new ResponseEntity<>("La contraseña actual es incorrecta", HttpStatus.BAD_REQUEST);
        } else {
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedNewPassword);
            userRepository.save(user);
            // notify via email
            emailService.sendEmail(user.getEmail(), "Contraseña actualizada", "Su contraseña ha sido actualizada con éxito");
            return new ResponseEntity<>("Contraseña actualizada con éxito", HttpStatus.OK);
        }
    }

    public void changeEmail(String username, String email) {
        User user = userRepository.findByUsername(username);
        user.setEmail(email);
        userRepository.save(user);
    }

    public void changeDescription(String username, String description) {
        User user = userRepository.findByUsername(username);
        user.setDescription(description);
        userRepository.save(user);
    }

    public void changeAlias(String username, String alias) {
        User user = userRepository.findByUsername(username);
        user.setAlias(alias);
        userRepository.save(user);
    }

    public ResponseEntity<String> changeProfileImage(User user, String profileImage) throws SQLException {
        user.setProfileImageFile(new SerialBlob(Base64.decodeBase64(profileImage)));
        userRepository.save(user);
        return new ResponseEntity<>("Imagen subida con éxito", HttpStatus.OK);

    }

    // Update multiple info at once (only makes 1 save)
    public void updateUserInfo(User user, String alias, String email, String description) {
        user.setAlias(alias);
        user.setEmail(email);
        user.setDescription(description);
        userRepository.save(user);
    }

    // Returns the whole list
    public List<Book> getList(User user, String listType) {
        return switch (listType) {
            case "read" -> user.getReadBooks();
            case "reading" -> user.getReadingBooks();
            case "wanted" -> user.getWantedBooks();
            default -> null;
        };
    }

    // Returns a page of the list
    public List<Book> getListPageable(User user, String listType, Pageable page) {

        return switch (listType) {
            case "read" -> userRepository.getReadBooks(user.getUsername(), page).getContent();
            case "reading" -> userRepository.getReadingBooks(user.getUsername(), page).getContent();
            case "wanted" -> userRepository.getWantedBooks(user.getUsername(), page).getContent();
            default -> null;
        };
    }

    public Page<User> getUsersPageable(String searchTerm, Pageable pageable) {
        return userRepository.searchUsers(searchTerm, pageable);
    }

    public void makeAuthor (User user) {
        if (user != null) {
            user.getRole().add("AUTHOR");
            userRepository.save(user);
        }
    }

    public String convertBooksToCSV(List<Book> books) {
        StringBuilder builder = new StringBuilder();
        builder.append("ID,Title,Author\n"); // CSV header
        for (Book book : books) {
            builder.append(book.getID()).append(",");
            builder.append(book.getTitle()).append(",");
            builder.append(book.getAuthor()).append("\n");
        }
        return builder.toString();
    }

    public List<Object[]> getUsersTotalReadings() {
        return userRepository.getUsersAndNumberOfBooksRead();
    }

    public boolean hasBookInList(User user, Book book, String listType) {
        return switch (listType) {
            case "read" -> user.getReadBooks().contains(book);
            case "reading" -> user.getReadingBooks().contains(book);
            case "wanted" -> user.getWantedBooks().contains(book);
            default -> false;
        };
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public List<Genre> getMostReadGenres(String username) {
        return userRepository.getMostReadGenres(username);
    }

    public List<Author> getMostReadAuthors(String username) {
        return userRepository.getMostReadAuthors(username);
    }

    public String getProfileImageStringByUsername(String username) {
        return userRepository.getProfileImageStringByUsername(username);
    }

    public void addBookToList(User user, Book book, String listType) {
        switch (listType) {
            case "read" -> user.addReadBook(book);
            case "reading" -> user.addReadingBook(book);
            case "wanted" -> user.addWantedBook(book);
        }
        userRepository.save(user);
    }

    public void removeBookFromList(User user, Book book, String listType) {
        switch (listType) {
            case "read" -> user.removeReadBook(book);
            case "reading" -> user.removeReadingBook(book);
            case "wanted" -> user.removeWantedBook(book);
        }
        userRepository.save(user);
    }

    public void moveBookToList(User user, Book book, String listType){
        switch (listType) {
            case "read" -> {
                user.addReadBook(book);
                user.removeReadingBook(book);
                user.removeWantedBook(book);
            }
            case "reading" -> {
                user.addReadingBook(book);
                user.removeReadBook(book);
                user.removeWantedBook(book);
            }
            case "wanted" -> {
                user.addWantedBook(book);
                user.removeReadBook(book);
                user.removeReadingBook(book);
            }
        }
        userRepository.save(user);
    }

    public void removeBookFromAllLists(User user, Book book) {
        user.removeReadBook(book);
        user.removeReadingBook(book);
        user.removeWantedBook(book);
        userRepository.save(user);
    }

    public boolean isUsernameAvailable(String username){
        return getUser(username) == null;
    }

}
