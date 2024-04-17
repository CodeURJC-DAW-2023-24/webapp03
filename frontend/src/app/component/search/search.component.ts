import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {UserService} from "../../services/user.service";
import {BookService} from "../../services/book.service";
import {SearchService} from "../../services/search.service";
import {User} from "../../models/user.model";
import {Book} from "../../models/book.model";
import {Router} from "@angular/router";

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

  constructor(
    private http: HttpClient, public userService: UserService, public bookService: BookService, private searchService: SearchService, private router: Router
  ) {

    this.userSearch = this.searchService.getUserSearch();

    this.searchService.getEvent().subscribe((event) => {
      this.searchQuery = event.query;

      if (this.userSearch) {

      this.userService.searchUsers(event.query, event.page).subscribe({
        next: n => {
          this.userQueries = n;
        },
        error: e => {
          console.log(e);
        }
      });
    } else {

        this.bookService.searchBooks(event.query, event.page).subscribe({
          next: n => {
            this.bookQueries = n;
          },
          error: e => {
            console.log(e);
          }
        });
    }

    });

  }

  showUser(username: string) {
    this.userService.getUser(username).subscribe({
      next: n => {
        this.router.navigate(["/profile"]);
      },
      error: e => {
        console.log(e);
      }
    });

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

  ngOnInit() {
    this.userSearch = this.searchService.getUserSearch();
  }

}

