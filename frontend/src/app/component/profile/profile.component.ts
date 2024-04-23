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

  readBooksListPage = 0;
  readingBooksListPage = 0;
  wantedBooksListPage = 0;

  noMoreReadBooks = false;
  noMoreReadingBooks = false;
  noMoreWantedBooks = false;

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
    this.listsService.getReadBooks(loggedUsername, this.readBooksListPage, 4).subscribe({
      next: books => {
        this.readBooks = books;
        this.readBooksListPage += 1;
      },
      error: r => {
        console.error("Error getting read books: " + JSON.stringify(r));
      }
    });
  }

  loadReadingList(loggedUsername:string) {
    this.listsService.getReadingBooks(loggedUsername, this.readingBooksListPage, 4).subscribe({
      next: books => {
        this.readingBooks = books;
        this.readingBooksListPage += 1;
      },
      error: r => {
        console.error("Error getting reading books: " + JSON.stringify(r));
      }
    });
  }

  loadWantedList(loggedUsername:string) {
    this.listsService.getWantedBooks(loggedUsername, this.wantedBooksListPage, 4).subscribe({
      next: books => {
        this.wantedBooks = books;
        this.wantedBooksListPage += 1;
      },
      error: r => {
        console.error("Error getting wanted books: " + JSON.stringify(r));
      }
    });
  }

  loadAllLists() {
    this.loadReadList(this.username);
    this.loadReadingList(this.username);
    this.loadWantedList(this.username);
  }

  loadMore(list: string) {
    if (list === "read") {

      let loadIcon = document.querySelector(".read-list-load-icon");
      loadIcon?.classList.add("fa-spin");


      this.listsService.getReadBooks(this.username, this.readBooksListPage, 4).subscribe({
        next: books => {
          this.readBooks = this.readBooks.concat(books);
          this.readBooksListPage += 1;

          loadIcon?.classList.remove("fa-spin");

          if(books.length === 0) {
            this.noMoreReadBooks = true;
          }
        },
        error: r => {
          console.error("Error getting read books: " + JSON.stringify(r));
        }
      });
    } else if (list === "reading") {

      let loadIcon = document.querySelector(".reading-list-load-icon");
      loadIcon?.classList.add("fa-spin");

      this.listsService.getReadingBooks(this.username, this.readingBooksListPage, 4).subscribe({
        next: books => {
          this.readingBooks = this.readingBooks.concat(books);
          this.readingBooksListPage += 1;

          loadIcon?.classList.remove("fa-spin");

          if(books.length === 0) {
            this.noMoreReadingBooks = true;
          }
        },
        error: r => {
          this.noMoreReadingBooks = true;
        }
      });
    } else if (list === "wanted") {

      let loadIcon = document.querySelector(".wanted-list-load-icon");
      loadIcon?.classList.add("fa-spin");

      this.listsService.getWantedBooks(this.username, this.wantedBooksListPage, 4).subscribe({
        next: books => {
          this.wantedBooks = this.wantedBooks.concat(books);
          this.wantedBooksListPage += 1;

          loadIcon?.classList.remove("fa-spin");

          if(books.length === 0) {
            this.noMoreWantedBooks = true;
          }
        },
        error: r => {
          this.noMoreWantedBooks = true;
        }
      });
    }
  }

  // Get book cover image
  bookImage(id: number) {
    return this.bookService.downloadCover(id);
  }
}
