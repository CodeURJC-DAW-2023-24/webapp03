import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {catchError} from "rxjs/operators";
import {throwError} from "rxjs";
import {Book} from "../models/book.model";
import {Genre} from "../models/genre.model";
import {LoginService} from "./session.service";
import {Author} from "../models/author.model";
import {User} from "../models/user.model";


@Injectable({
  providedIn: "root"
})
export class ListsService {

  constructor(private httpClient: HttpClient, private loginService: LoginService) {
  }

  setReadBooks(id: number) {
    return this.httpClient.post("/api/books/" + id + "/read", {}).pipe(
      catchError((error) => this.handleError(error))
    );
  }

  setReadingBooks(id: number) {
    return this.httpClient.post("/api/books/" + id + "/reading", {}).pipe(
      catchError((error) => this.handleError(error))
    );
  }

  setWantedBooks(id: number) {
    return this.httpClient.post("/api/books/" + id + "/wanted", {}).pipe(
      catchError((error) => this.handleError(error))
    );
  }

  setNoneBookList(id: number) {
    return this.httpClient.delete("/api/books/" + id + "/lists", {}).pipe(
      catchError((error) => this.handleError(error))
    );
  }

  // Get user's read book list
  getReadBooks(username: string, page: number, size: number): Observable<Book[]> {
    let params = new HttpParams().set("list", "read").set("page", page).set("size", size);
    return this.httpClient.get<Book[]>('/api/users/' + username + '/books', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Book[]>;
  }

  // Get user's reading book list
  getReadingBooks(username: string, page: number, size: number): Observable<Book[]> {
    let params = new HttpParams().set("list", "reading").set("page", page).set("size", size);
    return this.httpClient.get<Book[]>('/api/users/' + username + '/books', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Book[]>;
  }

  // Get user's wanted list
  getWantedBooks(username: string, page: number, size: number): Observable<Book[]> {
    let params = new HttpParams().set("list", "wanted").set("page", page).set("size", size);
    return this.httpClient.get<Book[]>('/api/users/' + username + '/books', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Book[]>;
  }

  // Get all user's lists as CSV
  getAllLists(username: string): Observable<string> {
    return this.httpClient.get('/api/users/' + username + '/export', {responseType: 'text'}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<string>;
  }

  isBookInList(username: string, bookID: number, list: string): Observable<boolean> {
    let params = new HttpParams().set("list", list);
    return this.httpClient.get<boolean>('/api/users/' + username + '/books/' + bookID, {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<boolean>;
  }


  private handleError(error: any) {
    console.log("ERROR: ");
    console.error(error);
    return throwError(() => new Error("Server error (" + error.status + "): " + error.statusText + ")"));
  }
}
