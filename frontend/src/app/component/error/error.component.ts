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
  selector: "app-error",
  templateUrl: "./error.component.html",
  styleUrls: ["./error.component.css", "../../../animations.css"],
})
export class ErrorComponent implements OnInit {
  constructor(private http: HttpClient, public bookService: BookService, public loginService: LoginService, public reviewService: ReviewService, public algorithmService: AlgorithmsService, public profileService: UserService) {

  }

  ngOnInit(): void {


  }



}
