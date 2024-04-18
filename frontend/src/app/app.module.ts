import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule} from '@angular/forms';

import {AppRoutingModule} from './app.routing.module';
import {AppComponent} from './app.component';
import {LandingComponent} from "./component/landing/landing.component";
import {FooterComponent} from "./component/footer/footer.component";
import {NavbarComponent} from "./component/navbar/navbar.component";
import {ProfileComponent} from "./component/profile/profile.component";
import {NgOptimizedImage} from "@angular/common";
import {BookComponent} from "./component/book/book.component";
import {SearchComponent} from "./component/search/search.component";
import {UserService} from "./services/user.service";
import {AdminComponent} from "./component/admin/admin.component";

@NgModule({
  declarations: [
    AppComponent,
    LandingComponent,
    NavbarComponent,
    SearchComponent,
    ProfileComponent,
    BookComponent,
    AdminComponent,
    FooterComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        FormsModule,
        NgOptimizedImage
    ],
  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule {
}
