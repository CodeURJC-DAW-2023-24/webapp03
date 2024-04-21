import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError} from "rxjs/operators";
import {Book} from "../models/book.model";

@Injectable({
  providedIn: "root"
})
export class BookService {
  private baseUrl = '/api/books/';
  private baseUrl2 = '/api/books';

  constructor(private httpClient: HttpClient) {
  }

  // Get specific book
  getBook(id: number): Observable<Book> {

    return this.httpClient.get<Book>(this.baseUrl + id).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Book>;
  }

  // Search books
  searchBooks(query: string, page: number): Observable<Map<string, Object>> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("query", query);
    params = params.append("page", page.toString());

    return this.httpClient.get<Map<string, Object>>(this.baseUrl2, {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Map<string, Object>>;
  }

  // Get all books
  getBooks(): Observable<Book[]> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("count", "false");

    return this.httpClient.get<Book[]>(this.baseUrl + "all", {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Book[]>;
  }

  //Get all books with count
  getBooksWithCount(): Observable<number> {

    let params = new HttpParams();
    params = params.append("count", "true"); // los parametros se pasan así mejor (los RequestParams)

    return this.httpClient.get<number>(this.baseUrl + "all", {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<number>;
  }

  //Delete specific book
  deleteBook(id: number | string): Observable<any> {

    return this.httpClient.delete(this.baseUrl + id).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<any>;
  }

  //Get book cover image as blob
  getCover(id: number): Observable<Blob> {
    return this.httpClient.get(this.baseUrl + id + "/image", {responseType: "blob"}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Blob>;
  }

  private handleError(error: any) {
    console.log("ERROR: ");
    console.error(error);
    return throwError(() => new Error("Server error (" + error.status + "): " + error.statusText + ")"));
  }

  // Download cover image to assets folder
  downloadCover(id: number) {
    return '/api/books/' + id + '/image';
  }

}
