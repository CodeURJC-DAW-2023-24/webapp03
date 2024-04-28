import {AfterViewChecked, AfterViewInit, Component, ElementRef, OnInit, QueryList, ViewChildren} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {UserService} from "../../services/user.service";
import {BookService} from "../../services/book.service";
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
export class SearchComponent implements AfterViewChecked {

  searchQuery: string = "";
  userSearch: boolean = false;
  page: number = 0;

  userQueries: User[] = [];
  bookQueries: Book[] = [];

  loadMoreBut!: HTMLElement;
  noResultsDiv!: HTMLElement;
  arrows!: HTMLElement;

  @ViewChildren("bookCard") books!: QueryList<ElementRef>;

  constructor(
    private http: HttpClient, public userService: UserService, public bookService: BookService, private router: Router, private activatedRoute: ActivatedRoute
  ) {

    this.activatedRoute.queryParams.subscribe(params => {
      //Empty all the queries, if there were any
      this.bookQueries = [];
      this.userQueries = [];

      //Set the page to 0
      this.page = 0;

      //On each page initialization, check if the user is searching for users or books
      this.userSearch = this.activatedRoute.snapshot.queryParams["users"] === "true";

      //Get the search query
      this.searchQuery = <string>this.activatedRoute.snapshot.queryParams["query"];

      this.search();

    });

  }

  userImage(username: string) {
    return this.userService.downloadProfilePicture(username);
  }

  bookImage(ID: number) {
    return this.bookService.downloadCover(ID);
  }

  search() {
    if (this.userSearch) {
      this.searchUsers(this.searchQuery);
    } else {
      this.searchBooks(this.searchQuery);
    }
  }

  searchUsers(query: string) {


    // @ts-ignore
    this.userService.searchUsers(query, this.page).subscribe({
      complete(): void {
      },
      next: (response: UserResponse) => {
        //Show load more button and hide no results div
        this.noResultsDiv.style.display = "none";
        this.loadMoreBut.style.display = "block";

        //Get the users and the total number of users with the query
        let users = response["users"] as User[];
        let maxUsers = response["total"] as number;

        //Show/Hide the load more button based on the number of users
        this.toggleLoadMore(SIZE * (this.page + 1), maxUsers);

        //Add more users to userQueries
        this.userQueries = this.userQueries.concat(users);

        //Increase page number
        this.page++;
      },
      error: (error) => {
        this.noResultsDiv.style.display = "block";
        this.loadMoreBut.style.display = "none";

        console.log(error);
      }
    });
  }

  searchBooks(query: string) {
    // @ts-ignore
    this.bookService.searchBooks(query, this.page).subscribe({
      complete(): void {
      },
      next: (response: BookResponse) => {
        //Show load more button and hide no results div
        this.noResultsDiv.style.display = "none";
        this.loadMoreBut.style.display = "block";

        //Get the books and the total number of books with the query
        let books = response["books"] as Book[];
        let maxBooks = response["total"] as number;

        //Show/Hide the load more button based on the number of books
        this.toggleLoadMore(SIZE * (this.page + 1), maxBooks);

        //Add more books to bookQueries
        this.bookQueries = this.bookQueries.concat(books);

        //Increase page number
        this.page++;
      },
      error: (error) => {
        this.noResultsDiv.style.display = "block";
        this.loadMoreBut.style.display = "none";

        console.log(error);
      }
    });
  }

  loadMore() {
    this.arrows.classList.add("fa-spin");

    setTimeout(() => {
      this.search();
      this.arrows.classList.remove("fa-spin");
    }, 300);
  }

  toggleLoadMore(currentSize: number, maxSize: number) {
    if (currentSize >= maxSize) {
      this.loadMoreBut.style.display = "none";
    } else {
      this.loadMoreBut.style.display = "block";
    }
  }

  putStars(rating: number, bookStars: any) {
    //This will find every star div of the rating container
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

  updateStars() {
    this.books.forEach((book, index) => {
      let bookStars = book.nativeElement.querySelectorAll("div")[3].children;
      this.bookService.putStars(this.bookQueries[index].averageRating, bookStars);
    });
  }

  ngAfterViewChecked() {
    this.updateStars();
    this.loadMoreBut = <HTMLElement> document.getElementById("loadMoreBut");
    this.noResultsDiv = <HTMLElement> document.getElementById("noResults");
    this.arrows = <HTMLElement> document.querySelector(".fa-arrows-rotate");
  }

}

