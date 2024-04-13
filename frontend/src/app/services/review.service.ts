import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {catchError} from "rxjs/operators";
import {throwError} from "rxjs";
import {UserLogin} from "../models/userLogin.model";
import {User} from "../models/user.model";
import {Review} from "../models/review.model";
import {LoginService} from "./session.service";

@Injectable({
  providedIn: "root"
})
export class ReviewService {
  private baseUrl = '/api/reviews/';
  private baseUrl2 = '/api/reviews';

  constructor(private httpClient: HttpClient, private loginService: LoginService) {
  }

  // Get specific review
  getReview(id: number): Observable<Review> {

    return this.httpClient.get<Review>(this.baseUrl + id).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Review>;
  }

  // Get all reviews for a book
  getReviews(page: number, size:number, bookID: number): Observable<Review[]> {

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("page", page);
    params = params.append("size", size);
    params = params.append("book", bookID);

    return this.httpClient.get<Review[]>(this.baseUrl, {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Review[]>;
  }

  //Get all reviews a user has made
  getReviewsByUser(username: string): Observable<Review[]> {
    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("count", "false");

    return this.httpClient.get<Review[]>(this.baseUrl + username, {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Review[]>;
  }

  //Get number of reviews a user has made
  getReviewCountByUser(username: string): Observable<number> {
    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("count", "true");

    return this.httpClient.get<number>(this.baseUrl + username, {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<number>;
  }

  //Delete specific review
  deleteReview(id: number): Observable<any> {

    return this.httpClient.delete(this.baseUrl + id).pipe(
      catchError(error => this.handleError(error))
    );
  }

  //Create review
  createReview(review: Review, bookID: number): Observable<Review> {

    //Check if user is logged in
    if (!this.loginService.isUserLogged()) {
      console.error("User is not logged in");
      return this.handleError("User is not logged in");
    }

    let params = new HttpParams(); // los parametros se pasan así mejor (los RequestParams)
    params = params.append("book", bookID);

    return this.httpClient.post<Review>(this.baseUrl2, review, {params: params}).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<Review>;
  }

  //Error handling
  private handleError(error: any) {
    console.error("Error in ReviewService: " + error);
    return throwError(error);
  }
}
