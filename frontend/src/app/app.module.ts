import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app.routing.module';
import {AppComponent} from './app.component';
import {LandingComponent} from "./component/landing/landing.component";
import {FooterComponent} from "./component/footer/footer.component";
import {NavbarComponent} from "./component/navbar/navbar.component";
import {ProfileComponent} from "./component/profile/profile.component";
import {NgOptimizedImage} from "@angular/common";

@NgModule({
  declarations: [
    AppComponent,
    LandingComponent,
    NavbarComponent,
    ProfileComponent,
    FooterComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        NgOptimizedImage
    ],
  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule {
}
