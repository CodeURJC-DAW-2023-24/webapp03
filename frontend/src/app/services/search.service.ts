import { Injectable } from '@angular/core';
import { Subject } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private userSearch = false;
  private eventSubject = new Subject<any>();

  emitEvent(event: any) {
    this.eventSubject.next(event);
  }

  getEvent() {
    return this.eventSubject.asObservable();
  }

  getUserSearch() {
    return this.userSearch;
  }

  setUserSearch(userSearch: boolean) {
    this.userSearch = userSearch;
  }

  constructor() { }

}
