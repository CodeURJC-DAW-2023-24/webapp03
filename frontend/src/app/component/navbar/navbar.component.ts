import {Component} from "@angular/core";
import {NavigationEnd, Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {NavbarService} from "../../services/navbar.service";
import {LoginService} from "../../services/session.service";
import {filter} from "rxjs/operators";

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

  onKeyDown(event: any) {
    if (event.key === "Enter") {
      this.search(event.target.value)
    }
  }

  search(query: string) {
    this.navbarService.setUserSearch(this.userSearch);
    this.router.navigate(["/search"], { queryParams: {query: query, users: this.userSearch} }).then(() => {
      this.navbarService.emitEvent({query: query, page: this.page, newSearch: true});
    });
  }

  profileImage(username: string) {
    return this.userService.downloadProfilePicture(username);
  }

  defaultImage() {
    return "assets/defaultProfilePicture.png";
  }

  toggleSearch(type: string) {
    let checkbox = document.getElementById("search-select");
    if (checkbox) {
      this.userSearch = type === "users";
      checkbox.setAttribute("checked", this.userSearch ? "true" : "false");
    }
  }

  ngOnInit() {
    let checkbox = document.getElementById("search-select");
    if (checkbox) {

      checkbox.onchange = () => {
        localStorage.setItem("userSearch", this.userSearch ? "true" : "false");
      }
    }

    //This event listens on each window load end (router) and sets the search checkbox to the last state
    this.router.events.pipe(filter((event) => event instanceof NavigationEnd)).subscribe(() => {

      let checkbox = localStorage.getItem("userSearch") === "true" ? document.getElementById("search-select") : null;
      if (checkbox && !this.userSearch) {
        checkbox.click();
      }
    });

  }
}
