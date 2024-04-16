import { Component } from "@angular/core";
import { AlgorithmsService } from "../../services/algorithms.service";
import { ReviewService } from "../../services/review.service";
import { LoginService } from "../../services/session.service";
import { BookService } from "../../services/book.service";
import { HttpClient } from "@angular/common/http";
import { UserService } from "../../services/user.service";

@Component({
  selector: "app-profile",
  templateUrl: "./profile.component.html",
  styleUrls: ["./profile.component.css"]
})
export class ProfileComponent {
  title = "Bookmarks";

  constructor(
    private http: HttpClient, public bookService: BookService, public loginService: LoginService, public reviewService: ReviewService, public algorithmService: AlgorithmsService, public userService: UserService
  ) {
    this.userService.getUser("admin").subscribe({
      next: n => {
        console.log(n);
      },
      error: e => {
        console.log(e);
      }
    });
  }
}
