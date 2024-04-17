import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { catchError } from "rxjs/operators";
import { throwError } from "rxjs";
import { User } from "../models/user.model";

const API_URL = "/api/users";

@Injectable({
  providedIn: "root"
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  getUser(username: string): Observable<User> {
    return this.http.get<User>(API_URL + "/" + username).pipe(
      catchError((error) => throwError(error))
    ) as Observable<User>;
  }

  searchUsers(username: string, page: number): Observable<User[]> {
    let params = new HttpParams().set("username", username).set("page", page.toString());
    return this.http.get<User[]>(API_URL, { params: params }).pipe(
      catchError((error) => throwError(error))
    ) as Observable<User[]>;
  }

  getUsers(): Observable<User[]> {
    let params = new HttpParams().set("page", "1");
    return this.http.get<User[]>(API_URL, { params: params }).pipe(
      catchError((error) => throwError(error))
    ) as Observable<User[]>;
  }

  downloadProfilePicture(username: string) {
    return '/api/users/' + username + '/image';
  }

}
