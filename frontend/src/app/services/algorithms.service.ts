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

type GenreCount = [string, number];
type AuthorCount = [string, number];
type BestReader = [string, number];


@Injectable({
  providedIn: "root"
})
export class AlgorithmsService {

  constructor(private httpClient: HttpClient, private loginService: LoginService) {
  }

  // Get most read books in general
  getMostReadGenresGeneral(): Observable<Genre[]> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("count", "false");

    return this.httpClient.get<Genre[]>('/api/genres', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Genre[]>;
  }


  // Get most read books in general (count)
  getMostReadGenresGeneralCount(): Observable<GenreCount[]> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("count", "true");

    return this.httpClient.get<GenreCount[]>('/api/genres', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<GenreCount[]>;
  }

  //Get most read genres from the current user
  private getMostReadGenresUser(): Observable<Genre[]> {
    // Check if user is logged in
    if (this.loginService.isUserLogged()) {
      return this.httpClient.get<Genre[]>('/api/genres/me').pipe(
        catchError(error => this.handleError(error))
      ) as Observable<Genre[]>;
    }
    return new Observable<Genre[]>();
  }

  // Get recommended books based on a user´s most read genres
  getRecommendedBooksForUser(page: number, size: number): Observable<Book[]> {
    // Check if user is logged in
    if (this.loginService.isUserLogged()) {

      let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
      params = params.append("by", "genre");
      params = params.append("page", page);
      params = params.append("size", size);

      return this.httpClient.get<Book[]>('/api/books/me/recommended', {params: params}).pipe(
        catchError(error => this.handleError(error))
      ) as Observable<Book[]>;
    }
    return new Observable<Book[]>();
  }

  // Get recommended books based on every users' most read genres
  getRecommendedBooksGeneral(page: number, size: number): Observable<Book[]> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("by", "genre");
    params = params.append("page", page);
    params = params.append("size", size);

    return this.httpClient.get<Book[]>('/api/books/recommended', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Book[]>;
  }

  // Get most read authors in general
  getMostReadAuthorsGeneral(): Observable<Author[]> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("count", "false");

    return this.httpClient.get<Author[]>('/api/authors', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Author[]>;
  }

  // Get most read authors in general (count)
  getMostReadAuthorsGeneralCount(): Observable<AuthorCount[]> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("count", "true");

    return this.httpClient.get<AuthorCount[]>('/api/authors', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<AuthorCount[]>;
  }

  //Get most read authors of a user
  private getMostReadAuthorsUser(): Observable<Author[]> {
    // Check if user is logged in
    if (this.loginService.isUserLogged()) {
      return this.httpClient.get<Author[]>('/api/authors/me').pipe(
        catchError(error => this.handleError(error))
      ) as Observable<Author[]>;
    }
    return new Observable<Author[]>();
  }

  // Get recommended books based on a user´s most read authors
  getRecommendedBooksForUserByAuthor(page: number, size: number): Observable<Book[]> {
    // Check if user is logged in
    if (this.loginService.isUserLogged()) {

      let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
      params = params.append("by", "author");
      params = params.append("page", page);
      params = params.append("size", size);

      return this.httpClient.get<Book[]>('/api/books/me/recommended', {params: params}).pipe(
        catchError(error => this.handleError(error))
      ) as Observable<Book[]>;
    }
    return new Observable<Book[]>();
  }

  // Get recommended books based on every users' most read authors
  getRecommendedBooksGeneralByAuthor(page: number, size: number): Observable<Book[]> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("by", "author");
    params = params.append("page", page);
    params = params.append("size", size);

    return this.httpClient.get<Book[]>('/api/books/recommended', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Book[]>;
  }

  // Get best readers
  getBestReaders(): Observable<BestReader[]> {
    return this.httpClient.get<BestReader[]>('/api/users/readings').pipe(
      catchError(error => this.handleError(error))
    ) as Observable<BestReader[]>;
  }

  // Get all registered users
  getUsers(): Observable<User> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("count", "false");

    return this.httpClient.get<User>('/api/users/all', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<User>;

  }

// Get all registered users (count)
  getUsersCount(): Observable<number> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("count", "true");

    return this.httpClient.get<number>('/api/users/all', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<number>;

  }

  // Get all authors
  getAuthors(): Observable<Author[]> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("count", "false");

    return this.httpClient.get<Author[]>('/api/authors/all', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Author[]>;

  }

  // Get all authors (count)
  getAuthorsCount(): Observable<number> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("count", "true");

    return this.httpClient.get<number>('/api/authors/all', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<number>;

  }

  // Get all genres
  getGenres(): Observable<Genre[]> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("count", "false");

    return this.httpClient.get<Genre[]>('/api/genres/all', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Genre[]>;

  }

  // Get all genres (count)
  getGenresCount(): Observable<number> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("count", "true");

    return this.httpClient.get<number>('/api/genres/all', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<number>;

  }

  // Get all books
  getBooks(): Observable<Book[]> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("count", "false");

    return this.httpClient.get<Book[]>('/api/books/all', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Book[]>;

  }

  // Get all books (count)
  getBooksCount(): Observable<number> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("count", "true");

    return this.httpClient.get<number>('/api/books/all', {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<number>;

  }

  private handleError(error: any) {
    console.log("ERROR: ");
    console.error(error);
    return throwError(() => new Error("Server error (" + error.status + "): " + error.statusText + ")"));
  }

}
