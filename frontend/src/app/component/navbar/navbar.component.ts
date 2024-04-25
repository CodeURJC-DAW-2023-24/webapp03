import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {UserService} from "../../services/user.service";
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

  loadedUser = false;

  isAdmin = false;

  constructor(private router: Router, public userService: UserService, private sessionService: LoginService, private activatedRoute: ActivatedRoute) {
    this.activatedRoute.params.subscribe(params => {
      if (this.router.url.includes("search")) {
        localStorage.setItem("userSearch", params['users'] === "true" ? "true" : "false");
      }
    });
  }

  onKeyDown(event: any) {
    if (event.key === "Enter") {
      this.search(event.target.value)
    }
  }

  search(query: string) {
    this.query = query;

    this.router.navigate(["/search", {users: this.userSearch, query: this.query}]);
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

    this.sessionService.checkLogged().subscribe({
      next: r => {
        this.loggedIn = r;
        if (this.loggedIn) {
          this.sessionService.getLoggedUser().subscribe({
            next: r => {
              this.loadedUser = true;
              this.username = r.username;

              if (r.roles.includes("ADMIN")) {
                this.isAdmin = true;
              }
            },
            error: r => {
              console.error("Error: " + JSON.stringify(r));
            }
          });
        }
      }
    });

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

  logout() {
    this.sessionService.logout().subscribe({
      next: r => {
        this.loadedUser = false;
        this.loggedIn = false;

        // reload the page
        window.location.reload();

      },
      error: r => {
        console.error("Error: " + JSON.stringify(r));
      }
    });
  }

}
