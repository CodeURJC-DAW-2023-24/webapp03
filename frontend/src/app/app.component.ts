import {Component} from '@angular/core';
import {Router, ActivatedRoute, NavigationEnd} from "@angular/router";
import {filter} from "rxjs/operators";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Bookmarks';

  showNavbarFooter: boolean = true;

  constructor(private router: Router, private activatedRoute: ActivatedRoute) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      // Check if the current route is the login or signup route
      this.showNavbarFooter = !(this.activatedRoute.firstChild?.snapshot.routeConfig?.path === 'login' ||
        this.activatedRoute.firstChild?.snapshot.routeConfig?.path === 'signup');
    });
  }
}
