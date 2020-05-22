import { Component, OnInit, Injectable, Input, Output } from '@angular/core';
import { StationsService } from '../stations.service';
import { EventEmitter } from '@angular/core';
import { BaseStationChangedService } from '../base-station-changed-service.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

  @Output() baseStationChanged = new EventEmitter<string>();
  baseStations = [];

  constructor(
    private baseStationService: StationsService,
    private stationChangedService: BaseStationChangedService
  ) { }

  ngOnInit(): void {
    this.baseStationService.getBaseStations()
      .then(result => this.baseStations = result);
  }

  onSelect(event: any) {
    console.log('Selection change to: ' + event.source.triggerValue);
    this.stationChangedService.baseStationChanged(event.source.triggerValue);
  }
}
