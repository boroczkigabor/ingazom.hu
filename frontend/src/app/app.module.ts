import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AgmCoreModule } from '@agm/core';
import { AuthComponent } from './auth/auth.component';
import { HomeComponent } from './home/home.component';
import { MapComponent } from './map/map.component';
import { NavbarComponent } from './navbar/navbar.component';
import { SettingsComponent } from './settings/settings.component';
import { SliderComponent } from './slider/slider.component';
import { WhatsthisComponent } from './whatsthis/whatsthis.component';

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
    AppRoutingModule,
    AgmCoreModule.forRoot({
      apiKey : 'AIzaSyAbR17tgc5xBl0T4vdjs-f6ZpVJRJlL1vk'
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
