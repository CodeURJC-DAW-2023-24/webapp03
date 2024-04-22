import {Component, OnInit} from "@angular/core";
import {AlgorithmsService} from "../../services/algorithms.service";
import {ReviewService} from "../../services/review.service";
import {LoginService} from "../../services/session.service";
import {BookService} from "../../services/book.service";
import {HttpClient} from "@angular/common/http";
import {UserService} from "../../services/user.service";
import {NavbarService} from "../../services/navbar.service";
import {ListsService} from "../../services/lists.service";
import {Book} from "../../models/book.model";

@Component({
  selector: "app-profile",
  templateUrl: "./profile.component.html",
  styleUrls: ["./profile.component.css"]
})
export class ProfileComponent implements OnInit {
  title = "Bookmarks";

  username = "";
  role = "";
  description = "";
  alias = "";
  email = "";

  loggedUser = this.loginService.getLoggedUsername();
  isCurrentUser = false;
  isAdministrator = this.loginService.isAdmin();
  isAuthor = this.loginService.isAuthor();

  readBooksCount = 0;
  readingBooksCount = 0;
  wantedBooksCount = 0;
  reviewCount = 0;

  //lists
  readBooks: Book[] = [];
  readingBooks: Book[] = [];
  wantedBooks: Book[] = [];

  constructor(
    private http: HttpClient, public bookService: BookService, public loginService: LoginService, public reviewService: ReviewService, public algorithmService: AlgorithmsService, public userService: UserService, private navbarService: NavbarService, private listsService: ListsService
  ) {

    this.navbarService.getEvent().subscribe((user) => {
      this.userService.getUser(user).subscribe({
        next: n => {
          this.username = n.username;
          this.role = n.roles[0];
          this.description = n.description;
          this.alias = n.alias;
          this.email = n.email;

          this

          this.isCurrentUser = this.loggedUser === this.username;
        },
        error: e => {
          console.log(e);
        }
      });

      this.userService.getReadBooksCount(user).subscribe({
        next: n => {
          this.readBooksCount = n;
        },
        error: e => {
          console.log(e);
        }
      });

      this.userService.getReadingBooksCount(user).subscribe({
        next: n => {
          this.readingBooksCount = n;
        },
        error: e => {
          console.log(e);
        }
      });

      this.userService.getWantedBooksCount(user).subscribe({
        next: n => {
          this.wantedBooksCount = n;
        },
        error: e => {
          console.log(e);
        }
      });

      this.reviewService.getReviewCountByUser(user).subscribe({
        next: n => {
          this.reviewCount = n;
        },
        error: e => {
          console.log(e);
        }
      });

    });

  }

  profileImage() {
    return this.userService.downloadProfilePicture(this.username);
  }

  ngOnInit() {
    this.loadAllLists();
  }

  loadReadList(loggedUsername:string) {
    this.listsService.getReadBooks(loggedUsername, 0, 4).subscribe({
      next: books => {
        this.readBooks = books;
      },
      error: r => {
        console.error("Error getting read books: " + JSON.stringify(r));
      }
    });
  }

  loadReadingList(loggedUsername:string) {
    this.listsService.getReadingBooks(loggedUsername, 0, 4).subscribe({
      next: books => {
        this.readingBooks = books;
      },
      error: r => {
        console.error("Error getting reading books: " + JSON.stringify(r));
      }
    });
  }

  loadWantedList(loggedUsername:string) {
    this.listsService.getWantedBooks(loggedUsername, 0, 4).subscribe({
      next: books => {
        this.wantedBooks = books;
      },
      error: r => {
        console.error("Error getting wanted books: " + JSON.stringify(r));
      }
    });
  }

  loadAllLists() {
    this.loadReadList(this.loggedUser);
    this.loadReadingList(this.loggedUser);
    this.loadWantedList(this.loggedUser);
  }

  // Get book cover image
  bookImage(id: number) {
    return this.bookService.downloadCover(id);
  }
}
