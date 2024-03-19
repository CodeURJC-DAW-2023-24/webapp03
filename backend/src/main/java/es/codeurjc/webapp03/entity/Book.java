package es.codeurjc.webapp03.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Book {
    //Data structure that will store example books (ID, Book title, Author, Description, Image, Date of publication, ISBN, Genre, Series, Page count, Publisher)

    public interface BasicInfo {}

    public interface AuthorInfo {}

    public interface GenreInfo {}

    public interface ReviewInfo {}

    @JsonView(BasicInfo.class)
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private long ID;

    @JsonView(BasicInfo.class)
    private String title;

    @JsonView(AuthorInfo.class)
    @ManyToMany
    private List<Author> author = new ArrayList<>();

    @JsonView(BasicInfo.class)
    private String authorString;

    @JsonView(BasicInfo.class)
    private String description;

    @JsonView(BasicInfo.class)
    private String releaseDate;

    @JsonView(BasicInfo.class)
    private String ISBN;

    @JsonView(BasicInfo.class)
    private double averageRating;

    @JsonView(BasicInfo.class)
    @Lob
    @JsonIgnore
    private Blob imageFile;

    @JsonIgnore // we will have to change this to a filter for creation of books
    private String imageString;

    @JsonView(GenreInfo.class)
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @JsonView(BasicInfo.class)
    private String series;

    @JsonView(BasicInfo.class)
    private int pageCount;

    @JsonView(BasicInfo.class)
    private String publisher;

    @JsonView(ReviewInfo.class)
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public Book() {
    }

    public Book(String title, String description, String imageString, String releaseDate, String ISBN, String series, int pageCount, String publisher) {
        this.title = title;
        this.description = description;
        this.imageString = imageString;
        this.releaseDate = releaseDate;
        this.ISBN = ISBN;
        this.series = series;
        this.pageCount = pageCount;
        this.publisher = publisher;
        this.averageRating = 0;
    }

    //ever used??
    private void loadReviews(List<Review> reviewList) {
        // Example reviews to test the functionality of the webpage

        /* General method to load all stored reviews for a specific book*/
        this.reviews = reviewList;
    }


    public List<Review> getReviews() {
        return this.reviews;
    }

    public List<Review> getReviewsRange(int start, int end) {

        if (end > this.reviews.size()) {
            end = this.reviews.size();
        }
        return this.reviews.subList(start, end);
    }

    public void setReview(int ID, Review review) {
        this.reviews.add(review);
    }

    // Getters for 'title'
    public String getTitle() {
        return this.title;
    }


    // Getter for 'author list'
    public List<Author> getAuthor() {
        return this.author;
    }


    // Getter for 'description'
    public String getDescription() {
        return this.description;
    }

    // Getter for 'image'
    /*public String getImage() {
        return this.image;
    }*/

    // Getter for 'release'
    public String getReleaseDate() {
        return this.releaseDate;
    }

    // Getter for 'ISBN'
    public String getISBN() {
        return this.ISBN;
    }

    // Getter for 'genre'
    public Genre getGenre() {
        return this.genre;
    }

    // Getter for 'series'
    public String getSeries() {
        return this.series;
    }

    // Getter for 'pageCount'
    public int getPageCount() {
        return this.pageCount;
    }

    // Getter for 'publisher'
    public String getPublisher() {
        return this.publisher;
    }

    // Getter for 'ID'
    public long getID() {
        return this.ID;
    }

    public double getAverageRating() {
        return this.averageRating;
    }

    //Setters
    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setAuthor(List<Author> author) {
        this.author = author;
        updateAuthorString();
    }


    public void setDescription(String description) {
        this.description = description;
    }

    /*public void setImage(String image) {
        this.image = image;
    }*/

    public void setReleaseDate(String release) {
        this.releaseDate = release;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void updateAuthorString() {
        String authorString = "";
        if (this.author.size() == 1) {
            authorString = this.author.get(0).getName();
        } else {
            for (int i = 0; i < this.author.size(); i++) {
                authorString += this.author.get(i).getName();
                if (i < this.author.size() - 1) {
                    authorString += ", ";
                }
            }
        }
        this.authorString = authorString;
    }

    public void addAuthor(Author author) {
        this.author.add(author);
        updateAuthorString();
    }

    public void removeAuthor(Author author) {
        this.author.remove(author);
        updateAuthorString();
    }

    public void removeReview(Review review) {
        this.reviews.remove(review);
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setAverageRating(double review) {
        this.averageRating = review;
    }

    public String getAuthorString() {
        return this.authorString;
    }

    public void setAuthorString(String authorString) {
        this.authorString = authorString;
    }

    //ToString method

    @Override
    public String toString() {
        // Manage authors (if there is only one author, print the name, if there are more than one, print the names separated by commas)
        String authorString = "";
        if (this.author.size() == 1) {
            authorString = this.author.get(0).getName();
        } else {
            for (int i = 0; i < this.author.size(); i++) {
                authorString += this.author.get(i).getName();
                if (i < this.author.size() - 1) {
                    authorString += ", ";
                }
            }
        }

        return "Book{" +
                "ID=" + ID +
                ", title='" + title + '\'' +
                ", author=" + authorString + '\'' +
                ", description='" + description + '\'' +
                //", image='" + image + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", genre=" + genre +
                ", series='" + series + '\'' +
                ", pageCount=" + pageCount +
                ", publisher='" + publisher + '\'' +
                '}';
    }

    public Blob getImageFile() {
        return this.imageFile;
    }

    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

    public String blobToString(Blob blob) throws SQLException{
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        String bookImage = Base64.getEncoder().encodeToString(bytes);
        return bookImage;
    }

    public Blob URLtoBlob(String webURL){
        try {
            URL url = new URL(webURL);
            InputStream in = url.openStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Read the image data into a byte array
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            in.close();
            // Convert the ByteArrayOutputStream to a byte array
            byte[] imageBytes = baos.toByteArray();
            Blob imageBlob = new SerialBlob(imageBytes);
            return imageBlob;
        } catch (IOException | SQLException e) {
            return null;
        }
    }

    public String getImageString() {
        return this.imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }
}
