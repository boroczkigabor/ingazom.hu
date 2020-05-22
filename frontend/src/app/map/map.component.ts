import { Component, OnInit } from '@angular/core';
import { config } from '../../environments/environment';
import { SettingsComponent } from '../settings/settings.component';
import { StationsService } from '../stations.service';
import { Station } from '../station';
import { BaseStationChangedService } from '../base-station-changed-service.service';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {
  lat: number;
  lng: number;
  zoom = 9;
  controlSize = 24;

  markersArray = [];
  baseStations = new Map<string, Station>();
  baseUrl = config.baseUrl;

  constructor(
    private settings: StationsService,
    private stationChangedService: BaseStationChangedService
  ) {
    this.stationChangedService.baseStationChanged$.subscribe(
      baseStation => {
        console.log('Selection change received: ' + baseStation);
        this.drawMap(baseStation);
      }
    );
   }

  ngOnInit(): void {
    this.initMap();
  }

  initMap() {
    const departureStation = 'BUDAPEST*'; // TODO retrieve from cookie or fallback
    this.getBaseStations()
      .then(result => result.forEach(item => this.baseStations.set(item.name, item)))
      .then(() => this.drawMap(departureStation));
  }

  getBaseStations() {
      return this.settings.getBaseStations();
  }

  drawMap(departureStation) {
    this.markersArray = [];
    const baseStation: Station = this.baseStations.get(departureStation);

    this.lat = baseStation.lat || 47.50022955;
    this.lng = baseStation.lon || 19.08387200;
    this.zoom = 9;

    let minimumMinute = 9999;

    this.settings.getDestinationsFor(baseStation)
          .then((data) => {
              data.forEach((item) => {
                  this.addMarker({lat: item.lat, lng: item.lon}, item);
                  const itemMinutes: number = item.minutes;
                  if (minimumMinute > itemMinutes) {
                    console.log('minimum: ' + itemMinutes);
                    minimumMinute = itemMinutes;
                  }
              });
              document.getElementById('minutesRange').setAttribute('min', minimumMinute.toString());
              this.doHideMarkers(55);
          });
  }

  addMarker(latLng, item) {
    let iconUrl = 'https://maps.google.com/mapfiles/ms/icons/';
    iconUrl += item.color + '-dot.png';

    const marker = {
      departure: item.departure,
      destination: item.destination,
      visibility: false,
      position: latLng,
      icon: iconUrl,
      minutes: parseInt(item.minutes, 10),
      url: item.elviraUrl
    };

    this.markersArray.push(marker);
  }

  showInfoWindow(infoWindow) {
      infoWindow.open();
  }

  hideInfoWindow(infoWindow) {
      infoWindow.close();
  }

  openUrlInNewTab(url: string) {
    const win = window.open(url, '_blank');
    win.focus();
  }

  doHideMarkers(minutes: number) {
    console.log('minutesRange: ' + minutes);
    this.markersArray.forEach((marker) => {
        marker.visibility = (marker.minutes <= minutes);
    });
  }

  hideMarkers(event) {
      const minutes = parseInt(event.target.value, 10);
      this.doHideMarkers(minutes);
  }
}
