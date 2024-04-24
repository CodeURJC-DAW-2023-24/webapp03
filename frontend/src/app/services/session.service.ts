import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {map, catchError} from "rxjs/operators";
import {throwError} from "rxjs";
import {UserLogin} from "../models/userLogin.model";
import {User} from "../models/user.model";
import { of } from "rxjs";

@Injectable({
  providedIn: "root"
})
export class LoginService {
  private baseUrl = '/api/auth/';
  private userUrl = '/api/users/';

  isLogged: boolean = false;
  user: User | undefined;

  constructor(private httpClient: HttpClient) {
  }

  checkLogged(): Observable<boolean> {

    return this.httpClient.get<User>(this.userUrl + "me", {withCredentials: true}).pipe(
      map(() => {
        this.isLogged = true;
        return true;
      }),
      catchError(error => {
        if (error.status === 401) {
          this.isLogged = false;
          this.user = undefined;
          return of(false);
        } else {
          return throwError(() => new Error("Server error (" + error.status + "): " + error.statusText + ")"));
        }
      })
    );

  }

  // if there is no error, set isLogged to true and user to the logged user
  login(user: UserLogin) {
    return this.httpClient.post(this.baseUrl + "login", user, {withCredentials: true}).pipe(
      catchError(error => this.wrongCredentialsError(error))
    );
  }

  logout(): Observable<any> {
    return this.httpClient.post(this.baseUrl + "logout", {withCredentials: true}).pipe(
      catchError(error => this.handleError(error))
    );
  }

  isUserLogged(): boolean {
    return this.isLogged;
  }

  getLoggedUser() {
    return this.httpClient.get<User>(this.userUrl + "me", {withCredentials: true}).pipe(
      catchError(error => this.handleError(error))
    );
  }

  getLoggedUsername(): string {
    return this.user?.username || "";
  }

  isAdmin(): boolean {
    if (this.user) {
      return this.user.roles.includes("ADMIN");
    }
    return false;
  }

  checkAdmin(): Observable<boolean> {
    return this.httpClient.get<User>(this.userUrl + "me", {withCredentials: true}).pipe(
      map((user) => {
        this.user = user;
        return user.roles.includes("ADMIN");
      }),
      catchError(error => {
        if (error.status === 401) {
          this.isLogged = false;
          this.user = undefined;
          return of(false);
        } else {
          return throwError(() => new Error("Server error (" + error.status + "): " + error.statusText + ")"));
        }
      })
    );
  }

  isAuthor(): boolean {
    if (this.user) {
      return this.user.roles.includes("AUTHOR");
    }
    return false;
  }

  checkAuthor(): Observable<boolean> {
    return this.httpClient.get<User>(this.userUrl + "me", {withCredentials: true}).pipe(
      map((user) => {
        this.user = user;
        return user.roles.includes("AUTHOR");
      }),
      catchError(error => {
        if (error.status === 401) {
          this.isLogged = false;
          this.user = undefined;
          return of(false);
        } else {
          return throwError(() => new Error("Server error (" + error.status + "): " + error.statusText + ")"));
        }
      })
    );
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
