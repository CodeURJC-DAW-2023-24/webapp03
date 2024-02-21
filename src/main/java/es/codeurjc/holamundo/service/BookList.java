package es.codeurjc.holamundo.service;

import es.codeurjc.holamundo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

@Service
public class BookList {
    //Data structure that will store example books (ID, Book title, Author, Description, Image, Date of publication, ISBN, Genre, Series, Page count, Publisher)
    private Map<Integer, BookC> books = new HashMap<>();

    @Autowired
    private BookRepository bookRepository;

    //Constructor
    public BookList() {
        //As this are example books to test the BookList functionality, we create individual books to extract the reviews from them.
        BookC book1 = new BookC(1, "The Hobbit", "J.R.R. Tolkien", "The Hobbit is a tale of high adventure, undertaken by a company of dwarves in search of dragon-guarded gold. A reluctant partner in this perilous quest is Bilbo Baggins, a comfort-loving unambitious hobbit, who surprises even himself by his resourcefulness and skill as a burglar.", "https://cdn.kobo.com/book-images/cf32789f-22db-4ad0-bba4-9c0bf69fb872/1200/1200/False/the-hobbit.jpg", "21 September 1937", "978-0-261-10221-4", "Fantasy", "Middle-earth", 310, "Molino");
        books.put(1, book1);

        BookC book2 = new BookC(2, "The Fellowship of the Ring", "J.R.R. Tolkien", "The Fellowship of the Ring is the first of three volumes of the epic novel The Lord of the Rings by the English author J.R.R. Tolkien. It is followed by The Two Towers and The Return of the King.", "https://m.media-amazon.com/images/I/813UBZ-O8sL._AC_UF1000,1000_QL80_.jpg", "29 July 1954", "978-0-261-10221-4", "Fantasy", "Middle-earth", 423, "Molino");
        books.put(2, book2);

        BookC book3 = new BookC(3, "The Two Towers", "J.R.R. Tolkien", "The Two Towers is the second volume of J.R.R. Tolkien's high fantasy novel The Lord of the Rings. It is preceded by The Fellowship of the Ring and followed by The Return of the King.", "https://m.media-amazon.com/images/I/81HfbQ8F2UL._AC_UF1000,1000_QL80_.jpg", "11 November 1954", "978-0-261-10221-4", "Fantasy", "Middle-earth", 352, "Molino");
        books.put(3, book3);

        BookC book4 = new BookC(4, "The Return of the King", "J.R.R. Tolkien", "The Return of the King is the third and final volume of J.R.R. Tolkien's The Lord of the Rings, following The Fellowship of the Ring and The Two Towers.", "https://m.media-amazon.com/images/I/81fMRrrUlqL._AC_UF1000,1000_QL80_.jpg", "20 October 1955", "978-0-261-10221-4", "Fantasy", "Middle-earth", 416, "Molino");
        books.put(4, book4);

        BookC book5 = new BookC(5, "The Hobbit", "J.R.R. Tolkien", "The Hobbit is a tale of high adventure, undertaken by a company of dwarves in search of dragon-guarded gold. A reluctant partner in this perilous quest is Bilbo Baggins, a comfort-loving unambitious hobbit, who surprises even himself by his resourcefulness and skill as a burglar.", "https://cdn.kobo.com/book-images/cf32789f-22db-4ad0-bba4-9c0bf69fb872/1200/1200/False/the-hobbit.jpg", "21 September 1937", "978-0-261-10221-4", "Fantasy", "Middle-earth", 310, "Molino");
        books.put(5, book5);

        BookC book6 = new BookC(6, "The Fellowship of the Ring", "J.R.R. Tolkien", "The Fellowship of the Ring is the first of three volumes of the epic novel The Lord of the Rings by the English author J.R.R. Tolkien. It is followed by The Two Towers and The Return of the King.", "https://m.media-amazon.com/images/I/813UBZ-O8sL._AC_UF1000,1000_QL80_.jpg", "29 July 1954", "978-0-261-10221-4", "Fantasy", "Middle-earth", 423, "Molino");
        books.put(6, book6);

        BookC book7 = new BookC(7, "The Two Towers", "J.R.R. Tolkien", "The Two Towers is the second volume of J.R.R. Tolkien's high fantasy novel The Lord of the Rings. It is preceded by The Fellowship of the Ring and followed by The Return of the King.", "https://m.media-amazon.com/images/I/81HfbQ8F2UL._AC_UF1000,1000_QL80_.jpg", "11 November 1954", "978-0-261-10221-4", "Fantasy", "Middle-earth", 352, "Molino");
        books.put(7, book7);

        BookC book8 = new BookC(8, "The Return of the King", "J.R.R. Tolkien", "The Return of the King is the third and final volume of J.R.R. Tolkien's The Lord of the Rings, following The Fellowship of the Ring and The Two Towers.", "https://m.media-amazon.com/images/I/81fMRrrUlqL._AC_UF1000,1000_QL80_.jpg", "20 October 1955", "978-0-261-10221-4", "Fantasy", "Middle-earth", 416, "Molino");
        books.put(8, book8);

        BookC book9 = new BookC(9, "The Two Towers", "J.R.R. Tolkien", "The Two Towers is the second volume of J.R.R. Tolkien's high fantasy novel The Lord of the Rings. It is preceded by The Fellowship of the Ring and followed by The Return of the King.", "https://m.media-amazon.com/images/I/81HfbQ8F2UL._AC_UF1000,1000_QL80_.jpg", "11 November 1954", "978-0-261-10221-4", "Fantasy", "Middle-earth", 352, "Molino");
        books.put(9, book9);

        BookC book10 = new BookC(10, "The Return of the King", "J.R.R. Tolkien", "The Return of the King is the third and final volume of J.R.R. Tolkien's The Lord of the Rings, following The Fellowship of the Ring and The Two Towers.", "https://m.media-amazon.com/images/I/81fMRrrUlqL._AC_UF1000,1000_QL80_.jpg", "20 October 1955", "978-0-261-10221-4", "Fantasy", "Middle-earth", 416, "Molino");
        books.put(10, book10);
    }

    //Method that will return the book list
    public Map<Integer, BookC> getBooks() {
        return books;
    }

    //Method that will return a specific book
    public BookC getBook(int bookID) {
        return books.get(bookID);
    }

    //Method that will add a book to the list
    public void addBook(BookC book) {
        books.put(books.size() + 1, book);
    }

    //Method that will remove a book from the list
    public void removeBook(int bookID) {
        books.remove(bookID);
    }

    //Method that will update a book from the list
    public void updateBook(int bookID, BookC book) {
        books.put(bookID, book);
    }

    //Method that will return the size of the book list
    public int getSize() {
        return books.size();
    }

}
