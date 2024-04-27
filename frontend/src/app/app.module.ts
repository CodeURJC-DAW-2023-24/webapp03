import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

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
import {LoginComponent} from "./component/login/login.component";
import {SignupComponent} from "./component/signUp/signup.component";
import {EditProfileComponent} from "./component/editProfile/editprofile.component";
import {ErrorComponent} from "./component/error/error.component";
import {LoginErrorComponent} from "./component/loginError/loginError.component";
import {CreateBookComponent} from "./component/createBook/createbook.component";
import {EditBookComponent} from "./component/editBook/editbook.component";

@NgModule({
  declarations: [
    AppComponent,
    LandingComponent,
    NavbarComponent,
    SearchComponent,
    ProfileComponent,
    EditProfileComponent,
    BookComponent,
    AdminComponent,
    LoginComponent,
    SignupComponent,
    ErrorComponent,
    FooterComponent,
    LoginErrorComponent,
    CreateBookComponent,
    EditBookComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        FormsModule,
        NgOptimizedImage,
        ReactiveFormsModule
    ],
  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule {
}
