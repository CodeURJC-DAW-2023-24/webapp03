import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {NavbarService} from "../../services/navbar.service";
import {LoginService} from "../../services/session.service";
import {filter} from "rxjs/operators";

@Component({
  selector: "app-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.css"]
})
export class NavbarComponent implements OnInit {
  page = 0;
  userSearch = false;
  loggedIn = false;
  username = "";
  query: string = "";

  constructor(private router: Router, public userService: UserService, private navbarService: NavbarService, private sessionService: LoginService, private activatedRoute: ActivatedRoute) {
    this.activatedRoute.params.subscribe(params => {
      if (this.router.url.includes("search")) {
        localStorage.setItem("userSearch", params['users'] === "true" ? "true" : "false");
      }
    });

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
    this.query = query;
    this.navbarService.setUserSearch(this.userSearch);
    let url = this.router.url;

      this.router.navigate(["/search", {

        users: this.userSearch,
        query: this.query

      }]).then(() => {
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
      setTimeout(() => {
        let currentState = checkbox.getAttribute("ng-reflect-model");

        if (currentState != localStorage.getItem("userSearch")) {
          checkbox.click();
        }
      }, 1);

      checkbox.onchange = () => {
        localStorage.setItem("userSearch", this.userSearch ? "true" : "false");
      }
    }
  }
}
