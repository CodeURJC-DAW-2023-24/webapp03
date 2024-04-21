import { Component } from "@angular/core";
import { AlgorithmsService } from "../../services/algorithms.service";
import { ReviewService } from "../../services/review.service";
import { LoginService } from "../../services/session.service";
import { BookService } from "../../services/book.service";
import { HttpClient } from "@angular/common/http";
import { UserService } from "../../services/user.service";
import { NavbarService } from "../../services/navbar.service";

@Component({
  selector: "app-profile",
  templateUrl: "./profile.component.html",
  styleUrls: ["./profile.component.css"]
})
export class ProfileComponent {
  title = "Bookmarks";

  username = "";
  role = "";
  description = "";
  alias = "";
  email = "";

  loggedUser = this.loginService.getLoggedUsername();
  isCurrentUser = false;
  isAdministrator = this.loginService.isAdmin();
  isAuthor = this.loginService.isAuthor();

  readBooksCount = 0;
  readingBooksCount = 0;
  wantedBooksCount = 0;

  constructor(
    private http: HttpClient, public bookService: BookService, public loginService: LoginService, public reviewService: ReviewService, public algorithmService: AlgorithmsService, public userService: UserService, private navbarService: NavbarService
  ) {

    this.navbarService.getEvent().subscribe((user) => {
      this.userService.getUser(user).subscribe({
        next: n => {
          this.username = n.username;
          this.role = n.roles[0];
          this.description = n.description;
          this.alias = n.alias;
          this.email = n.email;

          this

          this.isCurrentUser = this.loggedUser === this.username;
        },
        error: e => {
          console.log(e);
        }
      });
    });

  }

  profileImage() {
    return this.userService.downloadProfilePicture(this.username);
  }

}
