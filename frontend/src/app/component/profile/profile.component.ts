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
import {distinctUntilChanged} from "rxjs";
import {map} from "rxjs/operators";

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

    this.activatedRoute.params.pipe(
      map(params => params['username']),
      distinctUntilChanged()
    ).subscribe(username => {
      this.wantedBooksListPage = 0;
      this.readingBooksListPage = 0;
      this.readBooksListPage = 0;
      this.initialize(username);
    });


  }

  exportLists() {
    this.listsService.getAllLists(this.username).subscribe({
      next: n => {
        console.log(n);
        let blob = new Blob([n], {type: 'text/csv;charset=utf-8;'});
        let url = window.URL.createObjectURL(blob);
        let link = document.createElement('a');
        link.href = url;
        link.download = "books.csv";
        link.style.display = 'none';

        document.body.appendChild(link);

        link.click();

        document.body.removeChild(link);
      },
      error: e => {
        console.log(e);
      }
    });
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

  updateRole(username: string) {
    this.userService.getUser(username).subscribe({
      next: n => {
        if (n.roles.includes("ADMIN")) {
          this.role = "ADMINISTRADOR";
          if (n.roles.includes("AUTHOR")) {
            this.role = "AUTOR Y ADMINISTRADOR";
            this.isAuthor = true;
            this.isAdministrator = true;
          } else {
            this.isAuthor = false;
          }
        } else {
          this.role = "USUARIO";
          if (n.roles.includes("AUTHOR")) {
            this.role = "AUTOR";
            this.isAuthor = true;
          } else {
            this.isAuthor = false;
          }
        }
      },
      error: e => {
        console.log(e);
      }
    });
  }

  initialize(username: string) {
    this.userService.getUser(username).subscribe({
      next: n => {
        this.userLoaded = true;
        this.username = n.username;

        this.updateRole(username);

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
        // route to error page with error title and description as body
        this.router.navigate(['/error'], {
          queryParams: {
            title: "Este usuario no existe",
            description: "No hemos podido encontrar ningún usuario llamado " + username
          }

        });
      }
    });

    this.loginService.checkLogged().subscribe({
      next: r => {
        if (r) {
          this.loginService.getLoggedUser().subscribe({
            next: n => {
              if (n.roles.includes("ADMIN")) {
                this.isAdministrator = true;
              }
            },
            error: e => {
              console.log(e);
            }
          });
        }
      }
    });

  }

  checkIfListsDone() {
    this.noMoreReadBooks = this.readBooksCount === this.readBooks.length;
    this.noMoreReadingBooks = this.readingBooksCount === this.readingBooks.length;
    this.noMoreWantedBooks = this.wantedBooksCount === this.wantedBooks.length;
  }

  showExport() {
    let exportButton = document.getElementById("exportImport");
    if (exportButton) {
      exportButton.style.visibility = "visible";
    }
  }

  hideExport() {
    let exportButton = document.getElementById("exportImport");
    if (exportButton) {
      exportButton.style.visibility = "hidden";
    }
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

  logout() {
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

  banUser(username: string) {
    // timeout to allow the page to reload
    this.userService.banUser(username).subscribe({
      next: r => {
        this.router.navigate(['/error'], {
          queryParams: {
            title: "Usuario baneado",
            description: username + " ya no existe."
          }
        });
      },
      error: r => {
        this.router.navigate(['/error'], {
          queryParams: {
            title: "Usuario baneado",
            description: username + " ya no existe."
          }
        });
      }
    });
  }

  toggleAuthorRole(username: string
  ) {
    this.userService.toggleAuthorRole(username, {responseType: 'text'}).subscribe({
      next: r => {
        this.updateRole(username);
      },
      error: r => {
        this.updateRole(username);
      }
    });
  }
}
