import {Component, OnInit} from "@angular/core";
import {Chart, registerables} from "chart.js";

Chart.register(...registerables);

@Component({
  selector: "app-loginError",
  templateUrl: "./loginError.component.html",
  styleUrls: ["./loginError.component.css", "../../../animations.css"],
})

export class LoginErrorComponent implements OnInit {
  title = "Bookmarks";

  ngOnInit() {
  }

}
