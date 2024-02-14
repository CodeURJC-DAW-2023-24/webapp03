package es.codeurjc.holamundo.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

@Service
public class BookList {
    //Data structure that will store example books (ID, Book title, Author, Description, Image, Date of publication, ISBN, Genre, Series, Page count, Publisher)
    private Map<Integer, Book> books = new HashMap<>();

    //Constructor
    public BookList() {
        //As this are example books to test the BookList functionality, we create individual books to extract the reviews from them.
        Book book1 = new Book(1, "The Hobbit", "J.R.R. Tolkien", "The Hobbit is a tale of high adventure, undertaken by a company of dwarves in search of dragon-guarded gold. A reluctant partner in this perilous quest is Bilbo Baggins, a comfort-loving unambitious hobbit, who surprises even himself by his resourcefulness and skill as a burglar.", "https://cdn.kobo.com/book-images/cf32789f-22db-4ad0-bba4-9c0bf69fb872/1200/1200/False/the-hobbit.jpg", "21 September 1937", "978-0-261-10221-4", "Fantasy", "Middle-earth", 310, "Molino");
        books.put(1, book1);

        Book book2 = new Book(2, "The Fellowship of the Ring", "J.R.R. Tolkien", "The Fellowship of the Ring is the first of three volumes of the epic novel The Lord of the Rings by the English author J.R.R. Tolkien. It is followed by The Two Towers and The Return of the King.", "https://m.media-amazon.com/images/I/813UBZ-O8sL._AC_UF1000,1000_QL80_.jpg", "29 July 1954", "978-0-261-10221-4", "Fantasy", "Middle-earth", 423, "Molino");
        books.put(2, book2);

        Book book3 = new Book(3, "The Two Towers", "J.R.R. Tolkien", "The Two Towers is the second volume of J.R.R. Tolkien's high fantasy novel The Lord of the Rings. It is preceded by The Fellowship of the Ring and followed by The Return of the King.", "https://m.media-amazon.com/images/I/81HfbQ8F2UL._AC_UF1000,1000_QL80_.jpg", "11 November 1954", "978-0-261-10221-4", "Fantasy", "Middle-earth", 352, "Molino");
        books.put(3, book3);
        
        Book book4 = new Book(4, "The Return of the King", "J.R.R. Tolkien", "The Return of the King is the third and final volume of J.R.R. Tolkien's The Lord of the Rings, following The Fellowship of the Ring and The Two Towers.", "https://m.media-amazon.com/images/I/81fMRrrUlqL._AC_UF1000,1000_QL80_.jpg", "20 October 1955", "978-0-261-10221-4", "Fantasy", "Middle-earth", 416, "Molino");
        books.put(4, book4);

        Book book5 = new Book(5, "The Hobbit", "J.R.R. Tolkien", "The Hobbit is a tale of high adventure, undertaken by a company of dwarves in search of dragon-guarded gold. A reluctant partner in this perilous quest is Bilbo Baggins, a comfort-loving unambitious hobbit, who surprises even himself by his resourcefulness and skill as a burglar.", "https://cdn.kobo.com/book-images/cf32789f-22db-4ad0-bba4-9c0bf69fb872/1200/1200/False/the-hobbit.jpg", "21 September 1937", "978-0-261-10221-4", "Fantasy", "Middle-earth", 310, "Molino");
        books.put(5, book5);

        Book book6 = new Book(6, "The Fellowship of the Ring", "J.R.R. Tolkien", "The Fellowship of the Ring is the first of three volumes of the epic novel The Lord of the Rings by the English author J.R.R. Tolkien. It is followed by The Two Towers and The Return of the King.", "https://m.media-amazon.com/images/I/813UBZ-O8sL._AC_UF1000,1000_QL80_.jpg", "29 July 1954", "978-0-261-10221-4", "Fantasy", "Middle-earth", 423, "Molino");
        books.put(6, book6);

        Book book7 = new Book(7, "The Two Towers", "J.R.R. Tolkien", "The Two Towers is the second volume of J.R.R. Tolkien's high fantasy novel The Lord of the Rings. It is preceded by The Fellowship of the Ring and followed by The Return of the King.", "https://m.media-amazon.com/images/I/81HfbQ8F2UL._AC_UF1000,1000_QL80_.jpg", "11 November 1954", "978-0-261-10221-4", "Fantasy", "Middle-earth", 352, "Molino");
        books.put(7, book7);

        Book book8 = new Book(8, "The Return of the King", "J.R.R. Tolkien", "The Return of the King is the third and final volume of J.R.R. Tolkien's The Lord of the Rings, following The Fellowship of the Ring and The Two Towers.", "https://m.media-amazon.com/images/I/81fMRrrUlqL._AC_UF1000,1000_QL80_.jpg", "20 October 1955", "978-0-261-10221-4", "Fantasy", "Middle-earth", 416, "Molino");
        books.put(8, book8);
    }

    //Method that will return the book list
    public Map<Integer, Book> getBooks() {
        return books;
    }

    //Method that will return a specific book
    public Book getBook(int bookID) {
        return books.get(bookID);
    }

    //Method that will add a book to the list
    public void addBook(Book book) {
        books.put(books.size() + 1, book);
    }

    //Method that will remove a book from the list
    public void removeBook(int bookID) {
        books.remove(bookID);
    }

    //Method that will update a book from the list
    public void updateBook(int bookID, Book book) {
        books.put(bookID, book);
    }

    //Method that will return the size of the book list
    public int getSize() {
        return books.size();
    }

    public ArrayList<Book> getMatchingResults(String searchQuery, int page, int size) {
        ArrayList<Book> matchingBooks = new ArrayList<>();
        for (int i = page; i <= size; i++) {
            if (
                    (books.get(i).getTitle().toLowerCase().contains(searchQuery.toLowerCase())) ||
                    (books.get(i).getAuthor().toLowerCase().contains(searchQuery.toLowerCase())) ||
                    (String.valueOf(books.get(i).getID()).toLowerCase().contains(searchQuery.toLowerCase())) ||
                    (books.get(i).getISBN().toLowerCase().contains(searchQuery.toLowerCase())) ||
                    (books.get(i).getSeries().toLowerCase().contains(searchQuery.toLowerCase()))
                )
            {
                matchingBooks.add(books.get(i));
            }
        }
        return matchingBooks;
    }
}
