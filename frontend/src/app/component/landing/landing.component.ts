import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: "app-landing",
  templateUrl: "./landing.component.html",
  styleUrls: ["./landing.component.css"]
})
export class LandingComponent implements OnInit {
  title = "Bookmarks";

  constructor(private http: HttpClient) {
  }


ngOnInit() {
    this.http.get("/api/users?query=YourReader&page=0").subscribe(data => {
      console.log(data);
    });
  }
}
