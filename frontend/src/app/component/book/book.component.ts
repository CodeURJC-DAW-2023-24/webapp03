import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {BookService} from "../../services/book.service";
import {Book} from "../../models/book.model";
import {ReviewService} from "../../services/review.service";
import {UserService} from "../../services/user.service";
import {Genre} from "../../models/genre.model";
import {LoginService} from "../../services/session.service";
import {AlgorithmsService} from "../../services/algorithms.service";
import {ListsService} from "../../services/lists.service";
import {Router, ActivatedRoute} from "@angular/router";
import {User} from "../../models/user.model";
import {catchError} from "rxjs/operators";
import {throwError} from "rxjs";
import {Review} from "../../models/review.model";

@Component({
  selector: "app-book",
  templateUrl: "./book.component.html",
  styleUrls: ["./book.component.css"]
})

export class BookComponent implements OnInit {

  reviewList: Review[] = [];
  reviewListCurrentPage: number = 0;
  hasAnyReviews: boolean = false;
  noMoreReviews: boolean = false;

  // user's own review
  isReviewed: boolean = false;
  isReviewAuthor: boolean = false;
  reviewID: number = 0;
  reviewTitle = "";
  reviewAuthor: string = "";
  reviewRating: number = 0;
  reviewContent = "";

  missingReviewFields: boolean = false;

  username = "";
  loggedUser = false;
  isAdministrator = false;
  isAuthor = false;

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

  markedAsRead: boolean = false;
  markedAsReading: boolean = false;
  markedAsWanted: boolean = false;
  markedAsNone: boolean = false;
  listForBook = "";

  constructor(private http: HttpClient, public bookService: BookService, public loginService: LoginService, public reviewService: ReviewService, public listsService: ListsService, private route: ActivatedRoute) {
  }

  loadOwnReview() {
    this.reviewService.hasUserReviewed(this.username, this.ID).subscribe({
      next: r => {
        if (r != -1) {
          this.reviewService.getReview(r).subscribe({
            next: r => {
              this.isReviewed = true;
              this.reviewID = r.ID as number;
              this.reviewTitle = r.title;
              this.reviewAuthor = r.authorName as string;
              this.reviewRating = r.rating;
              this.reviewContent = r.content;
              this.isReviewAuthor = true;
            },
            error: err => {
              console.error(err);
            }
          });
        } else {
          this.isReviewed = false;
        }
      }
    });
  }

  initializeUser() {
    this.loginService.checkLogged().subscribe({
      next: r => {
        if (r) {
          this.loggedUser = true;
          this.loginService.getLoggedUser().subscribe({
            next: r => {
              this.username = r.username;
              this.isAdministrator = r.roles.includes("ADMIN");
              this.isAuthor = r.roles.includes("AUTHOR");
              this.isBookInList();
              this.loadOwnReview();
            },
            error: err => {
              console.error(err);
            }
          });
        }
      }
    });
  }

  loadReviews(bookID: number, page: number) {
    this.reviewService.getReviews(page, 3, bookID).subscribe({
      next: r => {
        this.reviewList = r;
        if (r.length == 0) {
          this.hasAnyReviews = false;
        } else {
          this.reviewListCurrentPage += 1;
          this.hasAnyReviews = true;
        }
      },
      error: err => {
        console.error(err);
      }
    });
  }

  loadMoreReviews() {
    this.reviewService.getReviews(this.reviewListCurrentPage, 3, this.ID).subscribe({
      next: r => {
        if (r.length == 0) {
          this.noMoreReviews = true;
        } else {
          this.reviewList.concat(r);
          this.reviewListCurrentPage += 1;
        }
      },
      error: err => {
        console.error(err);
      }
    });

  }


  ngOnInit(): void {
    this.ID = Number(this.route.snapshot.paramMap.get('id')); // Get book ID from URL
    this.initializeUser();
    this.getBookInfo();
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

        this.loadReviews(book.ID, this.reviewListCurrentPage);

      }
    });
  }

  bookImage() {
    return this.bookService.downloadCover(this.ID);
  }

  checkUserStatus() {
    return this.loginService.checkLogged();
  }

  markAsRead() {
    this.listsService.setReadBooks(this.ID).subscribe({
      next: () => {
        this.markedAsRead = true;
        this.markedAsReading = false;
        this.markedAsWanted = false;
        this.markedAsNone = false;
      },
      error: r => {
        console.log("Error adding book to list 'Read Books': " + JSON.stringify(r));
      }
    });
  }

  markAsReading() {
    this.listsService.setReadingBooks(this.ID).subscribe({
      next: () => {
        this.markedAsRead = false;
        this.markedAsReading = true;
        this.markedAsWanted = false;
        this.markedAsNone = false;
      },
      error: r => {
        console.log("Error adding book to list 'Reading Books': " + JSON.stringify(r));
      }
    });
  }

  markAsWanted() {
    this.listsService.setWantedBooks(this.ID).subscribe({
      next: () => {
        this.markedAsRead = false;
        this.markedAsReading = false;
        this.markedAsWanted = true;
        this.markedAsNone = false;
      },
      error: r => {
        console.log("Error adding book to list 'Wanted Books': " + JSON.stringify(r));
      }
    });
  }

  removeFromLists() {
    this.listsService.setNoneBookList(this.ID).subscribe({
      next: () => {
        this.markedAsRead = false;
        this.markedAsReading = false;
        this.markedAsWanted = false;
        this.markedAsNone = true;
      },
      error: r => {
        console.log("Error setting book to no list: " + JSON.stringify(r));
      }
    });
  }

  isBookInList() {
    const listNames: string[] = ["read", "reading", "wanted"];

    for (const listName of listNames) {
      this.listsService.isBookInList(this.username, this.ID, "read").subscribe({
        next: r => {
          if (r) {
            console.log("Book is in list " + listName);
            if (listName == "read") {
              this.markAsRead()
            } else if (listName == "reading") {
              this.markAsReading();
            } else {
              this.markAsWanted();
            }
          } else {
            console.log("Book not in list");
          }
        },
        error: err => {
          console.error(err);
        }
      });
    }
  }

  deleteReview(id: number | undefined) {
    this.reviewService.deleteReview(id as number).subscribe({
        next: r => {
          if (r == "Review deleted") {

            // delete review from list
            this.reviewList = this.reviewList.filter(r => r.ID != id);

            // Update average rating
            this.bookService.getBook(this.ID).subscribe({
              next: r => {
                this.averageRating = r.averageRating;
              },
              error: err => {
                console.error(err);
              }
            });


            // if the list got emptied, set hasAnyReviews to false
            if (this.reviewList.length == 0) {
              this.hasAnyReviews = false;
            }

            if (id == this.reviewID) {
              this.isReviewed = false;
              this.reviewID = 0;
              this.reviewTitle = "";
              this.reviewAuthor = "";
              this.reviewRating = 0;
              this.reviewContent = "";

            }
          } else {
            console.error("Error deleting review: " + r);
          }
        },
        error:
          err => {
            console.error(err);
          }

      }
    );
  }

  starClicked(rating: number) {
    for (let i = 1; i <= 5; i++) {
      const star = document.getElementById(`starIcon${i}`);
      if (star) {
        if (i <= rating) {
          star.classList.remove('fa-regular');
          star.classList.add('fa-solid');
        } else {
          star.classList.remove('fa-solid');
          star.classList.add('fa-regular');
        }
        this.reviewRating = rating;
      }
    }
  }

  addReview() {
    // Check if all fields are filled
    if (this.reviewTitle == "" || this.reviewRating == 0 || this.reviewContent == "") {
      this.missingReviewFields = true;
      return;
    }
    this.missingReviewFields = false;
    this.reviewService.createReview({
      title: this.reviewTitle,
      rating: this.reviewRating,
      content: this.reviewContent
    }, this.ID).subscribe({
      next: r => {
        this.reviewList.push(r);
        this.isReviewed = true;
        this.reviewID = r.ID as number;
        this.reviewAuthor = r.authorName as string;
        this.isReviewAuthor = true;
        this.reviewListCurrentPage = 0;
        this.reviewTitle = r.title;
        this.reviewRating = r.rating;
        this.reviewContent = r.content;
        this.isReviewed = true;
        this.noMoreReviews = false;

        // Update average rating
        this.bookService.getBook(this.ID).subscribe({
          next: r => {
            this.averageRating = r.averageRating;
          },
          error: err => {
            console.error(err);
          }
        });

      },
      error: err => {
        console.error(err);
      }
    });

  }

}
