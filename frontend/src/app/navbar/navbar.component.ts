import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SettingsComponent } from '../settings/settings.component';
import { WhatsthisComponent } from '../whatsthis/whatsthis.component';
import { BaseStationChangedService } from '../base-station-changed-service.service';
import { AuthComponent } from '../auth/auth.component';
import { ComponentType } from '@angular/cdk/portal';
import { AuthService, SocialUser } from 'angularx-social-login';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  dialogRef = undefined;
  user: SocialUser = undefined;
  signedIn = false;

  constructor(
    public dialog: MatDialog,
    private baseStationChangedService: BaseStationChangedService,
    private authService: AuthService
  ) {
    this.baseStationChangedService.baseStationChanged$.subscribe(
      () => {
        this.dialogRef?.close();
        this.dialogRef = undefined;
      });
    this.authService.authState.subscribe(user => {
      if (user) {
        this.user = user;
        this.signedIn = true;
        this.dialogRef?.close();
      }
    });
  }

  ngOnInit(): void {
  }

  resetUser() {
    this.signedIn = false;
    this.user = undefined;
  }

  avatarClicked() {
    if (this.isSignedIn()) {
      this.signOut();
    } else {
      this.showAuth();
    }
  }

  signOut(){
    this.authService.signOut(false);
    this.resetUser();
    console.log('User signed out.');
  }

  isSignedIn() {
    return this.signedIn;
  }

  showWhatsThisModal() {
    this.showModal(WhatsthisComponent);
  }

  showSettings() {
    this.showModal(SettingsComponent);
  }

  showAuth() {
    this.showModal(AuthComponent);
  }

  showModal(component: ComponentType<any>) {
    this.dialogRef = this.dialog.open(component);
  }

}
