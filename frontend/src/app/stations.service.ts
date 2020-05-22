import { Injectable, OnInit } from '@angular/core';
import { config } from '../environments/environment';
import { Station } from './station';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StationsService {
  private baseStationsLoaded = new Subject<void>();

  baseStations = [];
  baseStationsMap = new Map<string, Station>();
  constructor() {
    this.loadBaseStations();
  }

  loadBaseStations() {
    fetch(config.baseUrl + 'baseStations')
      .then(response => response.json())
      .then(data => data.forEach((item: Station) => {
        this.baseStations.push(item);
        this.baseStationsMap.set(item.name, item);
      }))
      .then(() => this.baseStationsLoaded.next());
  }

  whenLoaded() {
    return this.baseStationsLoaded.asObservable();
  }

  getBaseStations() {
    return this.baseStations;
  }

  clearBaseStations() {
    this.baseStations = [];
  }

  async getDestinationsFor(baseStation: Station): Promise<Station[]> {
    return fetch(config.baseUrl + 'destinationsForMap/' + baseStation.id)
      .then(response => response.json());
  }

  getStationByName(name: string) {
    return this.baseStationsMap.get(name);
  }

}
