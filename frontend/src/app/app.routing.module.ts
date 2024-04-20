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


const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'app', component: AppComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'book', component: BookComponent },
  { path: 'search', component: SearchComponent },
  { path: 'admin', component: AdminComponent },
  {path: 'login', component: LoginComponent},
  {path: 'signup', component: SignupComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
