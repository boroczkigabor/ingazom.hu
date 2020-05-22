import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SettingsComponent } from '../settings/settings.component';
import { WhatsthisComponent } from '../whatsthis/whatsthis.component';
import { BaseStationChangedService } from '../base-station-changed-service.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  dialogRef = undefined;

  constructor(
    public dialog: MatDialog,
    private baseStationChangedService: BaseStationChangedService
  ) {
    this.baseStationChangedService.baseStationChanged$.subscribe(
      () => {
        this.dialogRef?.close();
        this.dialogRef = undefined;
      });
  }

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
    this.dialogRef = this.dialog.open(component);
    this.dialogRef.afterClosed().subscribe(() => {
      console.log('The dialog was closed');
    });
  }
}
