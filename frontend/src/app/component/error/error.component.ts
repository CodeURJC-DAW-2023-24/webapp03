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
import {ActivatedRoute} from "@angular/router";

Chart.register(...registerables);

@Component({
  selector: "app-error",
  templateUrl: "./error.component.html",
  styleUrls: ["./error.component.css", "../../../animations.css"],
})
export class ErrorComponent implements OnInit {

  errorTitle: string = "";
  errorDescription: string = "";

  constructor(private http: HttpClient, public bookService: BookService, public loginService: LoginService, public reviewService: ReviewService, public algorithmService: AlgorithmsService, public profileService: UserService, private route: ActivatedRoute) {

  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      // if no parameters are passed, the default error message is displayed

      if (!params['title'] && !params['description']) {
        this.errorTitle = "Se ha producido un error";
        this.errorDescription = "Si ves esta pantalla muy a menudo, por favor, contacta con nosotros";
      } else {
        this.errorTitle = params['title'];
        this.errorDescription = params['description'];
      }

    });

  }


}
