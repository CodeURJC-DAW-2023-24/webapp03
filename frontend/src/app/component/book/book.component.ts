import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {BookService} from "../../services/book.service";
import {Book} from "../../models/book.model";
import {ReviewService} from "../../services/review.service";
import {UserService} from "../../services/user.service";
import {Genre} from "../../models/genre.model";
import {LoginService} from "../../services/session.service";
import {AlgorithmsService} from "../../services/algorithms.service";
import {NavbarService} from "../../services/navbar.service";
import {ListsService} from "../../services/lists.service";
import {Router, ActivatedRoute} from "@angular/router";
import {User} from "../../models/user.model";

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

  loggedUser = this.loginService.getLoggedUsername();
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

  constructor(private http: HttpClient, public bookService: BookService, public loginService: LoginService, public reviewService: ReviewService, public userService: UserService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.ID = Number(this.route.snapshot.paramMap.get('id')); // Obtiene el ID del libro desde la URL
    this.getBookInfo();
    this.getReviewInfo();
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
}
