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
import {Router} from "@angular/router";
import {toNumbers} from "@angular/compiler-cli/src/version_helpers";

Chart.register(...registerables);

@Component({
  selector: "app-createbook",
  templateUrl: "./createbook.component.html",
  styleUrls: ["./createbook.component.css", "../../../animations.css"]
})

export class CreateBookComponent {
  title = "Bookmarks"
  isAdmin = false;

  constructor(public bookService:BookService, private router:Router) {
  }

  ngOnInit() {
  }

  createBook(newTitle: string, newAuthor: string, newDescription: string, newDate: string,
             newRating: number, newSeries: string, newPageCount: string, newPublisher: string,
             newISBN: string, newGenre: string) {
    this.bookService.createBook({title: newTitle, authorString: newAuthor, description: newDescription, releaseDate: newDate,
                                        averageRating: newRating, series: newSeries, pageCount: +newPageCount, publisher: newPublisher,
                                        isbn: newISBN, genre: newGenre}).subscribe({
      next: r => {
        //Should get the book id and then redirect to that page
        this.router.navigate(["/"])
      },
      error: r => {
        console.error("Error creating book: " + JSON.stringify(r));
      }
    });
  }

}
