import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError} from "rxjs/operators";
import {Book} from "../models/book.model";
import {User} from "../models/user.model";

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

  //upload book
  createBook(bookData :{title: string, authorString: string, description: string, releaseDate: string,
                          averageRating: number, series: string, pageCount: number, publisher: string,
                          isbn: string, genre:string}):Observable<Book>{
    return this.httpClient.post<Book>(this.baseUrl2, bookData).pipe(
      catchError((error) =>
        throwError(error))
    ) as Observable<Book>
  }

  //Upload image
  uploadImage(id:number, file: File):Observable<string>{
    const formData = new FormData();
    formData.append("image",file)
    return this.httpClient.put(this.baseUrl + id + "/image",formData, {responseType: "text"}).pipe(
      catchError((error) => throwError(error))
    ) as Observable<string>
  }

  editBook(bookData :{title: string, authorString: string, description: string, releaseDate: string,
    averageRating: number, series: string, pageCount: number, publisher: string,
    isbn: string, genre:string}, id: number):Observable<Book>{
    return this.httpClient.put<Book>(this.baseUrl + id, bookData).pipe(
      catchError((error) =>
        throwError(error))
    ) as Observable<Book>
  }

  putStars(rating: number, bookStars: any) {
    //This will find every star div of the rating container
    if (rating >= 5.0) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-fill");
      bookStars[2].classList.add("bi-star-fill");
      bookStars[3].classList.add("bi-star-fill");
      bookStars[4].classList.add("bi-star-fill");
    } else if (rating >= 4.5) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-fill");
      bookStars[2].classList.add("bi-star-fill");
      bookStars[3].classList.add("bi-star-fill");
      bookStars[4].classList.add("bi-star-half");
    } else if (rating >= 4.0) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-fill");
      bookStars[2].classList.add("bi-star-fill");
      bookStars[3].classList.add("bi-star-fill");
      bookStars[4].classList.add("bi-star");
    } else if (rating >= 3.5) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-fill");
      bookStars[2].classList.add("bi-star-fill");
      bookStars[3].classList.add("bi-star-half");
      bookStars[4].classList.add("bi-star");
    } else if (rating >= 3.0) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-fill");
      bookStars[2].classList.add("bi-star-fill");
      bookStars[3].classList.add("bi-star");
      bookStars[4].classList.add("bi-star");
    } else if (rating >= 2.5) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-fill");
      bookStars[2].classList.add("bi-star-half");
      bookStars[3].classList.add("bi-star");
      bookStars[4].classList.add("bi-star");
    } else if (rating >= 2.0) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-fill");
      bookStars[2].classList.add("bi-star");
      bookStars[3].classList.add("bi-star");
      bookStars[4].classList.add("bi-star");
    } else if (rating >= 1.5) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star-half");
      bookStars[2].classList.add("bi-star");
      bookStars[3].classList.add("bi-star");
      bookStars[4].classList.add("bi-star");
    } else if (rating >= 1.0) {
      bookStars[0].classList.add("bi-star-fill");
      bookStars[1].classList.add("bi-star");
      bookStars[2].classList.add("bi-star");
      bookStars[3].classList.add("bi-star");
      bookStars[4].classList.add("bi-star");
    } else if (rating >= 0.5) {
      bookStars[0].classList.add("bi-star-half");
      bookStars[1].classList.add("bi-star");
      bookStars[2].classList.add("bi-star");
      bookStars[3].classList.add("bi-star");
      bookStars[4].classList.add("bi-star");
    } else {
      bookStars[0].classList.add("bi-star");
      bookStars[1].classList.add("bi-star");
      bookStars[2].classList.add("bi-star");
      bookStars[3].classList.add("bi-star");
      bookStars[4].classList.add("bi-star");
    }
  }
}
