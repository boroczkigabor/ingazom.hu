import { Component, OnInit, Injectable, Input } from '@angular/core';
import { StationsService } from '../stations.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

  @Input() selectedStation;
  baseStations = [];

  constructor(
    private baseStationService: StationsService
  ) { }

  ngOnInit(): void {
    this.baseStationService.getBaseStations()
      .then(result => this.baseStations = result);
  }

}
