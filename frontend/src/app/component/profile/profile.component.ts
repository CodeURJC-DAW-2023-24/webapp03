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
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: "app-profile",
  templateUrl: "./profile.component.html",
  styleUrls: ["./profile.component.css"]
})
export class ProfileComponent implements OnInit {
  title = "Bookmarks";

  userLoaded = false;

  username = "";
  role = "";
  description = "";
  alias = "";
  email = "";

  isCurrentUser = false;
  isAdministrator = false;
  isAuthor = false;

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
    private http: HttpClient, public bookService: BookService, public loginService: LoginService, public reviewService: ReviewService, public algorithmService: AlgorithmsService, public userService: UserService, private navbarService: NavbarService, private listsService: ListsService, private activatedRoute: ActivatedRoute, private router: Router
  ) {
    this.activatedRoute.params.subscribe(params => {
      this.initialize(params["username"]);
    });

    /*
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

     */

  }

  profileImage() {
    return this.userService.downloadProfilePicture(this.username);
  }

  checkIfUserMatches(urlUsername: string) {
    this.loginService.checkLogged().subscribe({
      next: r => {
        if (r) {
          this.loginService.getLoggedUser().subscribe({
            next: n => {
              this.isCurrentUser = n.username === urlUsername;
            },
            error: e => {
              console.log(e);
            }
          });
        }
      }
    });


  }

  initialize(username: string) {
    this.userService.getUser(username).subscribe({
      next: n => {
        this.userLoaded = true;
        this.username = n.username;
        this.role = n.roles[0];
        this.description = n.description;
        this.alias = n.alias;
        this.email = n.email;

        this.loadAllLists(username);

        this.userService.getReadBooksCount(username).subscribe({
          next: n => {
            this.readBooksCount = n;
          },
          error: e => {
            console.log(e);
          }
        });

        this.userService.getReadingBooksCount(username).subscribe({
          next: n => {
            this.readingBooksCount = n;
          },
          error: e => {
            console.log(e);
          }
        });

        this.userService.getWantedBooksCount(username).subscribe({
          next: n => {
            this.wantedBooksCount = n;
          },
          error: e => {
            console.log(e);
          }
        });

        this.reviewService.getReviewCountByUser(username).subscribe({
          next: n => {
            this.reviewCount = n;
          },
          error: e => {
            console.log(e);
          }
        });
      },
      error: e => {
        // route to error page
        this.router.navigate(['/error']);
      }
    });

    this.loginService.checkAdmin().subscribe({
      next: r => {
        this.isAdministrator = r;
      }
    });

    this.loginService.checkAuthor().subscribe({
      next: r => {
        this.isAuthor = r;
      }
    });

  }

  checkIfListsDone() {
    this.noMoreReadBooks = this.readBooksCount === this.readBooks.length;
    this.noMoreReadingBooks = this.readingBooksCount === this.readingBooks.length;
    this.noMoreWantedBooks = this.wantedBooksCount === this.wantedBooks.length;
  }


  ngOnInit() {

    // get username from url
    const urlUsername = this.activatedRoute.snapshot.params['username'];

    this.checkIfUserMatches(urlUsername);

    this.initialize(urlUsername);
  }

  loadReadList(loggedUsername: string) {
    this.listsService.getReadBooks(loggedUsername, this.readBooksListPage, 4).subscribe({
      next: books => {
        this.readBooks = books;
        this.readBooksListPage += 1;

        this.checkIfListsDone();
      },
      error: r => {
        console.error("Error getting read books: " + JSON.stringify(r));
      }
    });
  }

  loadReadingList(loggedUsername: string) {
    this.listsService.getReadingBooks(loggedUsername, this.readingBooksListPage, 4).subscribe({
      next: books => {

        this.readingBooks = books;
        this.readingBooksListPage += 1;

        this.checkIfListsDone();
      },
      error: r => {
        console.error("Error getting reading books: " + JSON.stringify(r));
      }
    });
  }

  loadWantedList(loggedUsername: string) {
    this.listsService.getWantedBooks(loggedUsername, this.wantedBooksListPage, 4).subscribe({
      next: books => {

        this.wantedBooks = books;
        this.wantedBooksListPage += 1;

        this.checkIfListsDone();
      },
      error: r => {
        console.error("Error getting wanted books: " + JSON.stringify(r));
      }
    });
  }

  loadAllLists(username: string) {
    this.loadReadList(username);
    this.loadReadingList(username);
    this.loadWantedList(username);
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

          this.checkIfListsDone();

          if (books.length === 0) {
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

          this.checkIfListsDone();

          if (books.length === 0) {
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

          this.checkIfListsDone();

          if (books.length === 0) {
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

  logout(){
    this.loginService.logout().subscribe({
      next: r => {
        // reload the page
        window.location.reload();
      },
      error: r => {
        console.error("Error: " + JSON.stringify(r));
      }
    });
  }
}
