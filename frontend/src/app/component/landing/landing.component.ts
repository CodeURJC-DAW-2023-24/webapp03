import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {BookService} from "../../services/book.service";
import {Book} from "../../models/book.model";
import {LoginService} from "../../services/session.service";
import {Review} from "../../models/review.model";
import {ReviewService} from "../../services/review.service";
import {AlgorithmsService} from "../../services/algorithms.service";
import {UserService} from "../../services/user.service";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {User} from "../../models/user.model";
import {Observable} from "rxjs";

@Component({
  selector: "app-landing",
  templateUrl: "./landing.component.html",
  styleUrls: ["./landing.component.css", "../../../animations.css"]
})
export class LandingComponent implements OnInit {
  title = "Bookmarks";
  totalSiteBooks: number | undefined;
  totalSiteGenres: number | undefined;
  totalSiteAuthors: number | undefined;
  totalSiteUsers: number | undefined;
  heroNameVisible = false;

  user: User | undefined;
  loggedUsername: string = "";
  loggedIn: boolean = false;
  isAdmin: boolean = false;

  recommendedBooksGenreLeft: Book[] = [];
  recommendedBooksGenreRight: Book[] = [];
  recommendedBooksAuthor: Book[] = [];

  constructor(private http: HttpClient, public bookService: BookService, public loginService: LoginService, public reviewService: ReviewService, public algorithmService: AlgorithmsService, public profileService: UserService) {

    this.algorithmService.getBooksCount().subscribe({
      next: n => {
        this.totalSiteBooks = n;
      },
      error: r => {
        console.error("Error getting total books count: " + JSON.stringify(r));
      }
    });

    this.algorithmService.getGenresCount().subscribe(
      next => {
        this.totalSiteGenres = next;
      },
    );

    this.algorithmService.getAuthorsCount().subscribe(
      next => {
        this.totalSiteAuthors = next;
      },
    );

    this.algorithmService.getUsersCount().subscribe(
      next => {
        this.totalSiteUsers = next;
      },
    );

    // If user is not logged in
    if (!this.loggedIn) {
      //Recommended book by author
      this.algorithmService.getRecommendedBooksGeneralByAuthor(0, 1).subscribe({
        next: books => {
          this.recommendedBooksAuthor = books;
        },
        error: r => {
          console.error("Error getting recommended books by author: " + JSON.stringify(r));
        }
      });

      //Recommended books by genre
      this.algorithmService.getRecommendedBooksGeneral(0, 4).subscribe({
        next: books => {
          this.recommendedBooksGenreLeft = books.slice(0, 2);
          this.recommendedBooksGenreRight = books.slice(2, 4);
        },
        error: r => {
          console.error("Error getting recommended books by genre: " + JSON.stringify(r));
        }

      });

    } else { // If user is logged in
      //Recommended book by author
      this.algorithmService.getRecommendedBooksForUserByAuthor(0, 1).subscribe({
        next: books => {
          this.recommendedBooksAuthor = books;
        },
        error: r => {
          console.error("Error getting recommended books by author: " + JSON.stringify(r));
        }
      });

      //Recommended books by genre
      this.algorithmService.getRecommendedBooksForUser(0, 4).subscribe({
        next: books => {
          this.recommendedBooksGenreLeft = books.slice(0, 2);
          this.recommendedBooksGenreRight = books.slice(2, 4);
        },
        error: r => {
          console.error("Error getting recommended books by genre: " + JSON.stringify(r));
        }
      });

    }
  }

  ngOnInit(): void {

    /* wait for login
    setTimeout(() => {
      this.checkIfLoggedIn();
    }, 1000);

     */


  }

  checkIfLoggedIn() {
    this.loginService.checkLogged();
    this.loggedIn = this.loginService.isUserLogged();
    console.log("Logged in: " + this.loggedIn);
    if (this.loggedIn) {
      this.user = this.loginService.getLoggedUser();
      this.loggedUsername = this.loginService.getLoggedUsername();

      // Check if user is admin
      this.isAdmin = this.loginService.isAdmin();

    }
  }

  // Get book cover image
  bookImage(id: number) {
    return this.bookService.downloadCover(id);
  }

  // Get user profile picture
  profilePicture(username: string) {
    return this.profileService.downloadProfilePicture(username);
  }


}
