import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {BookService} from "../../services/book.service";
import {Book} from "../../models/book.model";

@Component({
  selector: "app-landing",
  templateUrl: "./landing.component.html",
  styleUrls: ["./landing.component.css"]
})
export class LandingComponent implements OnInit {
  title = "Bookmarks";

  constructor(private http: HttpClient, public bookService: BookService) {
  }


  ngOnInit() {
    // testing api methods
    this.bookService.getBooks().subscribe(data => {
      console.log(data);
    });

    this.bookService.getBooksWithCount().subscribe(data => {
      console.log(data);
    });

    //get only the first's book ID
    this.bookService.getBook(5).subscribe(
      book => {
        console.log(book.authorString);
      }
    );

    //search for books
    this.bookService.searchBooks("Harry Potter", 0).subscribe(data => {
      console.log(data);
    });

    /*
    //Download book's cover
    this.bookService.downloadCover(5).subscribe(
      data => {
        const url = window.URL.createObjectURL(data);
        window.open(url);
      },
      error => {
        console.log(error);
    });
    */

  }
}
