import {Component} from "@angular/core";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {NavbarService} from "../../services/navbar.service";
import {LoginService} from "../../services/session.service";

@Component({
  selector: "app-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.css"]
})
export class NavbarComponent {
  page = 0;
  userSearch = false;
  loggedIn = false;
  username = "";

  constructor(private router: Router, public userService: UserService, private navbarService: NavbarService, private sessionService: LoginService) {
    this.loggedIn = sessionService.isUserLogged();
    if (this.loggedIn) {
      this.username = sessionService.getLoggedUsername();
    }
  }

  goToProfile() {
    this.router.navigate(["/profile"]).then(() => {
      this.navbarService.emitEvent(this.sessionService.getLoggedUsername());
    });
  }

  onKeyDown(event: any) {
    if (event.key === "Enter") {
      this.search(event.target.value)
    }
  }

  search(query: string) {
    this.navbarService.setUserSearch(this.userSearch);
    this.router.navigate(["/search"]).then(() => {
      this.navbarService.emitEvent({query: query, page: this.page, newSearch: true});
    });
  }

  profileImage(username: string) {
    return this.userService.downloadProfilePicture(username);
  }

  defaultImage() {
    return "assets/defaultProfilePicture.png";
  }

  goHome() {
    this.router.navigate(["/"]);
  }

  toggleSearch(type: string) {
    let checkbox = document.getElementById("search-select");
    if (checkbox) {
      this.userSearch = type === "users";
      checkbox.setAttribute("checked", this.userSearch ? "true" : "false");
    }
  }
}
