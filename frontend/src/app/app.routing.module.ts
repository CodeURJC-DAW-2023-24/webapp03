import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LandingComponent } from './component/landing/landing.component';
import { ProfileComponent } from './component/profile/profile.component';
import { BookComponent } from './component/book/book.component';
import { SearchComponent } from "./component/search/search.component";
import {AdminComponent} from "./component/admin/admin.component";
import {LoginComponent} from "./component/login/login.component";
import {SignupComponent} from "./component/signUp/signup.component";
import {EditProfileComponent} from "./component/editProfile/editprofile.component";
import {ErrorComponent} from "./component/error/error.component";
import {LoginErrorComponent} from "./component/loginError/loginError.component";
import {CreateBookComponent} from "./component/createBook/createbook.component";
import {EditBookComponent} from "./component/editBook/editbook.component";


const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'app', component: AppComponent },
  { path: 'profile/:username', component: ProfileComponent },
  { path: 'profile/:username/edit', component: EditProfileComponent},
  { path: 'book/:id', component: BookComponent },
  { path: 'search', component: SearchComponent },
  { path: 'admin', component: AdminComponent },
  { path: 'login', component: LoginComponent},
  { path: 'signup', component: SignupComponent},
  { path: 'error', component: ErrorComponent},
  { path: 'loginError', component: LoginErrorComponent},
  { path: 'admin/createBook', component: CreateBookComponent},
  { path: 'book/:id/edit', component: EditBookComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {scrollPositionRestoration: 'enabled'})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
