import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {BookService} from "../../services/book.service";
import {Book} from "../../models/book.model";
import {LoginService} from "../../services/session.service";
import {Review} from "../../models/review.model";
import {ReviewService} from "../../services/review.service";
import {AlgorithmsService} from "../../services/algorithms.service";
import {UserService} from "../../services/user.service";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {User} from "../../models/user.model";
import {Observable} from "rxjs";
import {Chart, registerables} from "chart.js";

Chart.register(...registerables);

@Component({
  selector: "app-login",
  templateUrl: "./signup.component.html",
  styleUrls: ["./signup.component.css", "../../../animations.css"],
})
export class SignupComponent implements OnInit {
  title = "Bookmarks";

  ngOnInit() {

  }

}
