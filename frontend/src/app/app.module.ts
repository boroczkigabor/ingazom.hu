import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { WhatsthisComponent } from './whatsthis/whatsthis.component';
import { SettingsComponent } from './settings/settings.component';
import { MapComponent } from './map/map.component';
import { AuthComponent } from './auth/auth.component';
import { SliderComponent } from './slider/slider.component';
import { HomeComponent } from './home/home.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    WhatsthisComponent,
    SettingsComponent,
    MapComponent,
    AuthComponent,
    SliderComponent,
    HomeComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
