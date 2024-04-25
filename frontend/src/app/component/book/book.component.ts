import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {BookService} from "../../services/book.service";
import {Book} from "../../models/book.model";
import {ReviewService} from "../../services/review.service";
import {UserService} from "../../services/user.service";
import {Genre} from "../../models/genre.model";
import {LoginService} from "../../services/session.service";
import {AlgorithmsService} from "../../services/algorithms.service";
import {ListsService} from "../../services/lists.service";
import {Router, ActivatedRoute} from "@angular/router";
import {User} from "../../models/user.model";
import {catchError} from "rxjs/operators";
import {throwError} from "rxjs";

@Component({
  selector: "app-book",
  templateUrl: "./book.component.html",
  styleUrls: ["./book.component.css"]
})

export class BookComponent implements OnInit{
  reviewID: number = 0;
  reviewTitle = "";
  reviewAuthor: string = "";
  reviewRating: number = 0;
  content = "";

  username = this.loginService.getLoggedUsername();
  loggedUser = this.loginService.isUserLogged();
  isCurrentUser = false;
  isAdministrator = this.loginService.isAdmin();
  isAuthor = this.loginService.isAuthor();

  book: Book | undefined; // Declara una variable para almacenar el libro
  ID: number = 0;
  genre: Genre | undefined;
  title = "";
  authorString = "";
  bookDescription = "";
  releaseDate = "";
  ISBN = "";
  averageRating = 0;
  series = "";
  pageCount = 0;
  publisher = "";

  markedAsRead: boolean = false;
  markedAsReading: boolean = false;
  markedAsWanted: boolean = false;
  markedAsNone: boolean = false;
  listForBook = "";

  constructor(private http: HttpClient, public bookService: BookService, public loginService: LoginService, public reviewService: ReviewService, public listsService: ListsService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.ID = Number(this.route.snapshot.paramMap.get('id')); // Get book ID from URL
    this.getBookInfo();
    this.getReviewInfo();
    this.isBookInList();
  }

  getReviewInfo() {
        return this.reviewService.getReviews(1, 3, this.ID)
  }

  getBookInfo(): void {
    this.bookService.getBook(this.ID).subscribe(book => {
      this.book = book;
      // Asigna los valores del libro a las variables correspondientes
      if (this.book) {
        this.genre = this.book.genre;
        this.title = this.book.title;
        this.authorString = this.book.authorString;
        this.bookDescription = this.book.description;
        this.releaseDate = this.book.releaseDate;
        this.ISBN = this.book.ISBN;
        this.averageRating = this.book.averageRating;
        this.series = this.book.series;
        this.pageCount = this.book.pageCount;
        this.publisher = this.book.publisher;
      }
    });
  }

  bookImage() {
    return this.bookService.downloadCover(this.ID);
  }

  checkUserStatus() {
    return this.loginService.checkLogged();
  }

  markAsRead() {
    this.listsService.setReadBooks(this.ID).subscribe({
      next: () => {
        this.markedAsRead = true;
        this.markedAsReading = false;
        this.markedAsWanted = false;
        this.markedAsNone = false;
      },
      error: r => {
        console.log("Error adding book to list 'Read Books': " + JSON.stringify(r));
      }
    });
  }

  markAsReading() {
    this.listsService.setReadingBooks(this.ID).subscribe({
      next: () => {
        this.markedAsRead = false;
        this.markedAsReading = true;
        this.markedAsWanted = false;
        this.markedAsNone = false;
      },
      error: r => {
        console.log("Error adding book to list 'Reading Books': " + JSON.stringify(r));
      }
    });
  }

  markAsWanted() {
    this.listsService.setWantedBooks(this.ID).subscribe({
      next: () => {
        this.markedAsRead = false;
        this.markedAsReading = false;
        this.markedAsWanted = true;
        this.markedAsNone = false;
      },
      error: r => {
        console.log("Error adding book to list 'Wanted Books': " + JSON.stringify(r));
      }
    });
  }

  removeFromLists(){
    this.listsService.setNoneBookList(this.ID).subscribe({
      next: () => {
        this.markedAsRead = false;
        this.markedAsReading = false;
        this.markedAsWanted = false;
        this.markedAsNone = true;
      },
      error: r => {
        console.log("Error setting book to no list: " + JSON.stringify(r));
      }
    });
  }

  isBookInList() {
    const listNames: string[] = ["read", "reading", "wanted"];

    for (const listName of listNames) {
      this.listsService.isBookInList(this.username, this.ID, "read").subscribe({
        next: r => {
          if(r) {
            console.log("Book is in list " + listName);
            if (listName == "read"){
              this.markAsRead()
            } else if(listName == "reading") {
              this.markAsReading();
            } else {
              this.markAsWanted();
            }
          } else {
            console.log("Book not in list");
          }
        },
        error: err => {
          console.error(err);
        }
      });
    }
  }
}
