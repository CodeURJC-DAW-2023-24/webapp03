import {Component} from "@angular/core";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {SearchService} from "../../services/search.service";

@Component({
  selector: "app-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.css"]
})
export class NavbarComponent {
  page = 0;
  userSearch = false;

  constructor(private router: Router, public userService: UserService, private searchService: SearchService) {
  }

  goToProfile() {
    this.router.navigate(["/profile"]);
  }

  onKeyDown(event: any) {
    if (event.key === "Enter") {
      this.search(event.target.value)
    }
  }

  search(query: string) {
    this.searchService.setUserSearch(this.userSearch);
      this.router.navigate(["/search"]).then(() => {
        this.searchService.emitEvent({query: query, page: this.page});
        this.page++;
      });
    }
}
