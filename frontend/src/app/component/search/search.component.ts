import {AfterViewChecked, Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {UserService} from "../../services/user.service";
import {BookService} from "../../services/book.service";
import {NavbarService} from "../../services/navbar.service";
import {User} from "../../models/user.model";
import {Book} from "../../models/book.model";
import {Router, ActivatedRoute} from "@angular/router";

const SIZE = 4;

interface BookResponse {
  books: Book[];
  total: number;
}

interface UserResponse {
  users: User[];
  total: number;
}

@Component({
  selector: "app-search",
  templateUrl: "./search.component.html",
  styleUrls: ["./search.component.css"]
})
export class SearchComponent implements OnInit {
  userQueries: User[] = [];
  bookQueries: Book[] = [];
  userSearch = false;
  searchQuery = "";
  page = 0;
  maxUsers = 0;
  maxBooks = 0;
  newSearch = false;


  constructor(
    private http: HttpClient, public userService: UserService, public bookService: BookService, private navbarService: NavbarService, private router: Router, private activatedRoute: ActivatedRoute

  ) {

    this.getMaxElems(true);

    this.navbarService.getEvent().subscribe((event) => {
      if (event.newSearch) {
        this.bookQueries = [];
        this.userQueries = [];

        this.toggleLoadMore(this.userSearch, []);
      }

      this.userSearch = this.navbarService.getUserSearch();
      this.searchQuery = event.query;
      localStorage.setItem("userSearch", this.userSearch ? "true" : "false");
      localStorage.setItem("searchQuery", this.searchQuery);

      this.maxBooks = parseInt(localStorage.getItem("maxBooks")!);
      this.maxUsers = parseInt(localStorage.getItem("maxUsers")!);

      if (this.userSearch) {

        // @ts-ignore
        this.userService.searchUsers(event.query, event.page).subscribe({
          complete(): void {
          },
          next: (response: UserResponse) => {
            this.newSearch = event.newSearch;
            let users = response["users"] as User[];
            this.maxUsers = response["total"] as number;

            this.toggleLoadMore(this.userSearch, users);
            localStorage.setItem("maxUsers", this.maxUsers.toString());

            if (users.length > 0) {
              document.getElementById("noResults")!.style.display = "none";

              this.userQueries = this.userQueries.concat(users);


            }
          },
          error: e => {
            document.getElementById("noResults")!.style.display = "block";
          }
        });
      } else {

        // @ts-ignore
        this.bookService.searchBooks(event.query, event.page).subscribe({
          complete(): void {
          },
          next: (response: BookResponse) => {
            this.newSearch = event.newSearch;
            let books = response["books"] as Book[];
            this.maxBooks = response["total"] as number;

            this.toggleLoadMore(this.userSearch, books);

            let pos = 0;
            localStorage.setItem("maxBooks", this.maxBooks.toString());

            if (books.length > 0) {
              let booksDiv = document.getElementById("books");
              if (booksDiv) {
                let observer = new MutationObserver((mutations) => {
                  mutations.forEach((mutation) => {
                    if (mutation.type === 'childList') {
                      for (let i = 0; i < mutation.addedNodes.length; i++) {
                        let newNode = mutation.addedNodes[i];
                        let book = books[pos];
                        this.putStars(book.averageRating, newNode);
                        pos++;
                      }
                    }
                  });
                  pos = 0;
                });

                let config = {childList: true};

                observer.observe(booksDiv, config);
              }

              document.getElementById("noResults")!.style.display = "none";

              this.bookQueries = this.bookQueries.concat(books);
            }
          },
          error: e => {
            document.getElementById("noResults")!.style.display = "block";
            console.log(e);
          }
        });
      }

    });

  }

  showUser(username: string) {
    this.userService.getUser(username).subscribe({
      next: n => {
        this.router.navigate(["/profile", username]).then(() => {
          this.navbarService.emitEvent(username);
        });
      },
      error: e => {
        console.log(e);
      }
    });

  }

  userImage(username: string) {
    return this.userService.downloadProfilePicture(username);
  }

  showBook(ID: number) {
    this.bookService.getBook(ID).subscribe({
      next: n => {
        this.router.navigate(["/book", ID]);
      },
      error: e => {
        console.log(e);
      }
    });
  }

  bookImage(ID: number) {
    return this.bookService.downloadCover(ID);
  }

  loadMoreBooks() {
    this.page++;
    this.navbarService.emitEvent({query: this.searchQuery, page: this.page, newSearch: false});
  }

  ngOnInit() {

    const storedQuery = this.activatedRoute.snapshot.queryParams['query'];
    const storedUserSearch = this.activatedRoute.snapshot.queryParams['users'];

    localStorage.setItem("userSearch", storedUserSearch ? "true" : "false");
    localStorage.setItem("searchQuery", storedQuery);

    if (storedQuery && storedUserSearch) {
      this.searchQuery = storedQuery;
      this.userSearch = JSON.parse(storedUserSearch);

      // Searches with the stored data
      this.navbarService.setUserSearch(this.userSearch);

      window.onload = () => {
        this.navbarService.emitEvent({query: this.searchQuery, page: this.page, newSearch: false});
      }

    }
  }

  getMaxElems(updated: boolean) {
    if (updated) {
      this.bookService.getBooksWithCount().subscribe({
        next: count => {
          this.maxBooks = count;
        },
        error: e => {
          console.log(e);
        }
      });

      this.userService.getUserCount().subscribe({
        next: count => {
          this.maxUsers = count;
        },
        error: e => {
          console.log(e);
        }
      });
    } else {
      this.maxBooks = localStorage.getItem("maxBooks") ? parseInt(localStorage.getItem("maxBooks")!) : 0;
      this.maxUsers = localStorage.getItem("maxUsers") ? parseInt(localStorage.getItem("maxUsers")!) : 0;
    }
  }

  toggleLoadMore(userSearch: boolean, elems: any) {

    if (userSearch) {
      if (((this.page + 1) * SIZE >= this.maxUsers) || elems.length < 4) {
        document.getElementById("loadMoreBut")!.style.display = "none";
      } else {
        document.getElementById("loadMoreBut")!.style.display = "block";
      }
    } else {
      if (((this.page + 1) * SIZE >= this.maxBooks) || elems.length < 4) {
        document.getElementById("loadMoreBut")!.style.display = "none";
      } else {
        document.getElementById("loadMoreBut")!.style.display = "block";
      }
    }

  }

  putStars(rating: number, book: any) {
    //This will find every star div of the rating container
    let bookStars = book.querySelectorAll("div")[4].children;
    if (rating >= 5.0) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-fill");
      bookStars[2].classList.add("bi-star-fill");
      bookStars[3].classList.add("bi-star-fill");
      bookStars[4].classList.add("bi-star-fill");
    } else if (rating >= 4.5) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-fill");
      bookStars[2].classList.add("bi-star-fill");
      bookStars[3].classList.add("bi-star-fill");
      bookStars[4].classList.add("bi-star-half");
    } else if (rating >= 4.0) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-fill");
      bookStars[2].classList.add("bi-star-fill");
      bookStars[3].classList.add("bi-star-fill");
      bookStars[4].classList.add("bi-star");
    } else if (rating >= 3.5) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-fill");
      bookStars[2].classList.add("bi-star-fill");
      bookStars[3].classList.add("bi-star-half");
      bookStars[4].classList.add("bi-star");
    } else if (rating >= 3.0) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-fill");
      bookStars[2].classList.add("bi-star-fill");
      bookStars[3].classList.add("bi-star");
      bookStars[4].classList.add("bi-star");
    } else if (rating >= 2.5) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-fill");
      bookStars[2].classList.add("bi-star-half");
      bookStars[3].classList.add("bi-star");
      bookStars[4].classList.add("bi-star");
    } else if (rating >= 2.0) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-fill");
      bookStars[2].classList.add("bi-star");
      bookStars[3].classList.add("bi-star");
      bookStars[4].classList.add("bi-star");
    } else if (rating >= 1.5) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-half");
      bookStars[2].classList.add("bi-star");
      bookStars[3].classList.add("bi-star");
      bookStars[4].classList.add("bi-star");
    } else if (rating >= 1.0) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star");
      bookStars[2].classList.add("bi-star");
      bookStars[3].classList.add("bi-star");
      bookStars[4].classList.add("bi-star");
    } else if (rating >= 0.5) {
      bookStars[0].classList.add("bi-star-half");
      bookStars[1].classList.add("bi-star");
      bookStars[2].classList.add("bi-star");
      bookStars[3].classList.add("bi-star");
      bookStars[4].classList.add("bi-star");
    } else {
      bookStars[0].classList.add("bi-star");
      bookStars[1].classList.add("bi-star");
      bookStars[2].classList.add("bi-star");
      bookStars[3].classList.add("bi-star");
      bookStars[4].classList.add("bi-star");
    }

  }

}

