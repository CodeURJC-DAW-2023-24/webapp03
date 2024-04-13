import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {catchError} from "rxjs/operators";
import {throwError} from "rxjs";
import {UserLogin} from "../models/userLogin.model";
import {User} from "../models/user.model";

@Injectable({
  providedIn: "root"
})
export class LoginService {
  private baseUrl = '/api/auth/';
  private userUrl = '/api/users/';

  isLogged: boolean = false;
  user: User | undefined;

  constructor(private httpClient: HttpClient) {
    this.checkLogged();
  }

  checkLogged() {

    this.httpClient.get(this.userUrl + "me", {withCredentials: true}).subscribe(
      data => {
        this.user = data as User;
        this.isLogged = true;
      },
      error => {
        if (error.status != 404) {
          console.error("Error checking if user is logged in. User is probably not logged in");
        }
      }
    );
  }

  login(user: UserLogin) {
    return this.httpClient.post(this.baseUrl + "login", user, {withCredentials: true}).subscribe(
      data => {
        this.checkLogged();
      },
      error => {
        this.wrongCredentialsError(error);
      }
    );
  }

  logout() {
    this.httpClient.post(this.baseUrl + "logout", {withCredentials: true}).subscribe(
      data => {
        console.log("Logged out");
        this.isLogged = false;
        this.user = undefined;
      },
      error => {
        console.error("Error logging out: " + JSON.stringify(error));
      }
    );
  }

  isUserLogged(): boolean {
    return this.isLogged;
  }

  getLoggedUser(): User | undefined {
    return this.user;
  }

  getLoggedUsername(): string{
    return this.user?.username || "";
  }

  isAdmin(): boolean {
    if (this.user) {
      return this.user.roles.includes("ADMIN");
    }
    return false;
  }


  //Custom error handlers

  private wrongCredentialsError(error: any) {
    console.log("ERROR: ");
    console.error(error);
    return throwError(() => new Error("Wrong credentials"));

  }

  private handleError(error: any) {
    console.log("ERROR: ");
    console.error(error);
    return throwError(() => new Error("Server error (" + error.status + "): " + error.statusText + ")"));
  }


}
