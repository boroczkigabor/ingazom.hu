import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SettingsComponent } from '../settings/settings.component';
import { WhatsthisComponent } from '../whatsthis/whatsthis.component';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  constructor(
    public dialog: MatDialog
  ) { }

  ngOnInit(): void {
  }

  showWhatsThisModal() {
    this.showModal(WhatsthisComponent);
  }

  showSettings() {
    this.showModal(SettingsComponent);
  }

  saveSettings() {
    this.hideModal('settings');
  }

  hideModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.style.display = 'none';
  }

  showModal(component) {
    const dialogRef = this.dialog.open(component);
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }
}
