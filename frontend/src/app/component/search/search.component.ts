import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {UserService} from "../../services/user.service";
import {BookService} from "../../services/book.service";
import {NavbarService} from "../../services/navbar.service";
import {User} from "../../models/user.model";
import {Book} from "../../models/book.model";
import {Router} from "@angular/router";

const SIZE = 4;

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

  constructor(
    private http: HttpClient, public userService: UserService, public bookService: BookService, private navbarService: NavbarService, private router: Router
  ) {

    this.navbarService.getEvent().subscribe((event) => {
      if (event.newSearch) {
        this.bookQueries = [];
        this.userQueries = [];
      }

      console.log((this.page + 1) * SIZE >= this.maxBooks)

      this.userSearch = this.navbarService.getUserSearch();
      this.searchQuery = event.query;
      localStorage.setItem("userSearch", this.userSearch ? "true" : "false");
      localStorage.setItem("searchQuery", this.searchQuery);

      if (this.userSearch) {

        if ((this.page + 1) * SIZE >= this.maxUsers) {
          document.getElementById("loadMoreBut")!.style.display = "none";
        } else {
          document.getElementById("loadMoreBut")!.style.display = "block";
        }

        this.userService.searchUsers(event.query, event.page).subscribe({
          next: users => {
            if (users.length > 0) {
              document.getElementById("noResults")!.style.display = "none";
              this.userQueries = this.userQueries.concat(users);
            }
          },
          error: e => {
            document.getElementById("noResults")!.style.display = "block";
            console.log(e);
          }
        });
      } else {

        if ((this.page + 1) * SIZE >= this.maxBooks) {
          document.getElementById("loadMoreBut")!.style.display = "none";
        } else {
          document.getElementById("loadMoreBut")!.style.display = "block";
        }

        this.bookService.searchBooks(event.query, event.page).subscribe({
          next: books => {
            if (books.length > 0) {
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
        this.router.navigate(["/profile"]).then(() => {
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
        this.router.navigate(["/book"]);
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
    this.navbarService.emitEvent({query: this.searchQuery, page: this.page});
  }

  ngOnInit() {
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

    window.onload = () => {
      const storedQuery = localStorage.getItem('searchQuery');
      const storedUserSearch = localStorage.getItem('userSearch');

      if (storedQuery && storedUserSearch) {
        this.searchQuery = storedQuery;
        this.userSearch = JSON.parse(storedUserSearch);

        // Searches with the stored data
        this.navbarService.setUserSearch(this.userSearch);
        this.navbarService.emitEvent({query: this.searchQuery, page: this.page});
      }
    }
  }
}

