package es.codeurjc.webapp03.service;

import es.codeurjc.webapp03.entity.Author;
import es.codeurjc.webapp03.entity.Book;
import es.codeurjc.webapp03.entity.Genre;
import es.codeurjc.webapp03.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DependsOn("authorService")
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    public Book getBook(long id) {
        return bookRepository.findByID(id);
    }

    public List<Double> getRatings(long id) {
        return bookRepository.getRatingsByBookId(id);
    }

    public List<Author> getAuthors(long id) {
        return bookRepository.findByID(id).getAuthor();
    }

    public String getAuthorsString(long id) {
        return bookRepository.findByID(id).getAuthorString();
    }

    public void delete(long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> getBooksByGenreIn(List<Genre> genres, Pageable pageable) {
        return bookRepository.findByGenreIn(genres, pageable).getContent();
    }

    public List<Book> getBooksByAuthor(String author, Pageable pageable) {
        return bookRepository.findByAuthorString(author, pageable).getContent();
    }

    public long getTotalBooks() {
        return bookRepository.count();
    }

    public Page<Book> searchBook(String searchQuery, Pageable pageable) {
        return bookRepository.searchBooks(searchQuery, pageable);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
