import { Component, OnInit } from '@angular/core';
import { config } from '../../environments/environment';

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
  baseStations = new Map();
  baseUrl = config.baseUrl;

  constructor() { }

  ngOnInit(): void {
    this.initMap();
  }

  initMap() {
    const departureStation = 'BUDAPEST*'; // TODO retrieve from cookie or fallback
    this.changeDropDownTextTo(departureStation);
    this.getBaseStations()
      .then(() => this.drawMap(departureStation));

  }

  changeDropDownTextTo(text: string) {
    const element = document.getElementById('dropbtn');
    element.innerHTML = text;
  }

  getBaseStations() {
      return fetch(this.baseUrl + 'baseStations')
          .then(response => response.json())
          .then(data => {
              data.forEach( (item) => {
                  this.baseStations.set(item.name, item);
                  this.addNewSelectionItem(item.name);
              });
          });
  }

  addNewSelectionItem(text: string) {
    const newA = document.createElement('a');
    newA.href = '#';
    newA.id = 'item-' + text;
    newA.text = text;
    newA.setAttribute('onclick', 'select(this)');

    document.getElementById('stationSelection').appendChild(newA);
  }

  drawMap(departureStation) {
    const baseStation = this.baseStations.get(departureStation);

    this.lat = baseStation.lat || 47.50022955;
    this.lng = baseStation.lon || 19.08387200;
    this.zoom = 9;

    let minimumMinute = 9999;

    fetch(this.baseUrl + 'destinationsForMap/' + baseStation.id)
          .then(response => response.json())
          .then(data => {
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
