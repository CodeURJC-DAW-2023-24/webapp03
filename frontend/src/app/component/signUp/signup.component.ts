import {Component, OnInit} from "@angular/core";
import {UserService} from "../../services/user.service";
import {Chart, registerables} from "chart.js";
import {Router} from "@angular/router";

Chart.register(...registerables);

@Component({
  selector: "app-login",
  templateUrl: "./signup.component.html",
  styleUrls: ["./signup.component.css", "../../../animations.css"],
})
export class SignupComponent implements OnInit {
  title = "Bookmarks";

  constructor(public userService:UserService, private router:Router) {

  }

  ngOnInit() {

  }

  signUp(userName:string, userAlias:string, userEmail:string, userPassword:string) {
    this.userService.singup({username:userName, email:userEmail, alias:userAlias, password:userPassword}).subscribe({
      next: r => {
        this.router.navigate(["/login"])
      },
      error: r => {
        console.error("Singup failed: " + JSON.stringify(r));
      }
    });
  }

}
