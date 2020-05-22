import { Injectable, OnInit } from '@angular/core';
import { config } from '../environments/environment';
import { Station } from './station';

@Injectable({
  providedIn: 'root'
})
export class StationsService {

  baseStations = [];
  constructor() { }

  async loadBaseStations() {
    const response = await fetch(config.baseUrl + 'baseStations');
    const data = await response.json();
    data.forEach((item) => {
      this.baseStations.push(item);
    });
  }

  async getBaseStations() {
    if (this.baseStations.length === 0) {
      await this.loadBaseStations();
    }
    return this.baseStations;
  }

  clearBaseStations() {
    this.baseStations = [];
  }

  async getDestinationsFor(baseStation: Station): Promise<Station[]> {
    return fetch(config.baseUrl + 'destinationsForMap/' + baseStation.id)
      .then(response => response.json());
  }

}
