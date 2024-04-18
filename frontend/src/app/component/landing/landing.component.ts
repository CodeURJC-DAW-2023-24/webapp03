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
import {Chart, registerables} from "chart.js";

Chart.register(...registerables);

@Component({
  selector: "app-landing",
  templateUrl: "./landing.component.html",
  styleUrls: ["./landing.component.css", "../../../animations.css"],
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

  loadingMoreBooks = false;
  noMoreBooks = false;
  page: number = 0;
  size: number = 4;

  recommendedBooksGenreLeft: Book[] = [];
  recommendedBooksGenreRight: Book[] = [];
  recommendedBooksAuthor: Book[] = [];

  loadingChart = true;
  public chart: any;

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
      this.algorithmService.getRecommendedBooksGeneral(this.page, this.size).subscribe({
        next: books => {
          this.recommendedBooksGenreLeft = books.slice(0, 2);
          this.recommendedBooksGenreRight = books.slice(2, 4);
          this.page += 1;
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
      this.algorithmService.getRecommendedBooksForUser(this.page, this.size).subscribe({
        next: books => {
          this.recommendedBooksGenreLeft = books.slice(0, 2);
          this.recommendedBooksGenreRight = books.slice(2, 4);
          this.page += 1;
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

    this.loadChart();


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

  loadMoreBooks() {
    this.loadingMoreBooks = true;
    if (!this.loggedIn) {
      this.algorithmService.getRecommendedBooksGeneral(this.page, this.size).subscribe({
        next: books => {
          // add new books to the lists
          this.recommendedBooksGenreLeft = this.recommendedBooksGenreLeft.concat(books.slice(0, 2));
          this.recommendedBooksGenreRight = this.recommendedBooksGenreRight.concat(books.slice(2, 4));
          this.page += 1;
          this.loadingMoreBooks = false;

          //if no books books are returned, hide the button and show a message
          if (books.length == 0) {
            this.noMoreBooks = true;
          }
        },
        error: r => {
          console.error("Error getting recommended books by genre: " + JSON.stringify(r));
        }
      });
    } else {
      this.algorithmService.getRecommendedBooksForUser(this.page, this.size).subscribe({
        next: books => {
          // add new books to the lists
          this.recommendedBooksGenreLeft = this.recommendedBooksGenreLeft.concat(books.slice(0, 2));
          this.recommendedBooksGenreRight = this.recommendedBooksGenreRight.concat(books.slice(2, 4));
          this.page += 1;
          this.loadingMoreBooks = false;
        },
        error: r => {
          console.error("Error getting recommended books by genre: " + JSON.stringify(r));
        }
      });
    }
  }


  // Load chart
  loadChart() {

    // Get the chart data

    let genreNames: string[] = []; // list for the genre names
    let genreCounts: number[] = []; // list for the genre counts
    // Build the chart for the most read genres (using getMostReadGenresGeneralCount())
    this.algorithmService.getMostReadGenresGeneralCount().subscribe({
      next: genres => {
        genres.forEach(g => {
          genreNames.push(g[0]); // the genre names is the first element of the tuple
          genreCounts.push(g[1]); // the genre count is the second element of the tuple

        });
        // Create the chart

        new Chart("mostReadGenresChart", {
          type: 'bar',
          data: {
            labels: genreNames,
            datasets: [{
              label: 'Número de libros leídos',
              data: genreCounts,
              backgroundColor: '#519E8A',
              borderWidth: 1
            }]
          },
          options: {
            scales: {
              y: {
                beginAtZero: true,
                ticks: {
                  callback: function (value, index, values) {
                    return Math.round(value as number);
                  }
                },
                grid: {
                  display: false
                }
              },
              x: {
                grid: {
                  display: false
                }
              }
            }
          }
        });

        this.loadingChart = false;
      },
      error: r => {
        console.error("Error getting most read genres: " + JSON.stringify(r));
      }
    });
  }


}
