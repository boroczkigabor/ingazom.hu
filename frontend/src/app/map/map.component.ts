import { Component, OnInit } from '@angular/core';
import { config } from '../../environments/environment';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {
  map;
  markersArray = [];
  baseStations = new Map();
  // let baseUrl = 'http://localhost:5000';
  baseUrl = config.baseUrl;
  constructor() { }

  ngOnInit(): void {
  }

  initMap() {
    const departureStation = 'BUDAPEST*'; // TODO retrieve from cookie or fallback
    this.changeDropDownTextTo(departureStation);
    this.getBaseStations()
      .then(() => this.drawMap(departureStation));

  }

  changeDropDownTextTo(text) {
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

  addNewSelectionItem(text) {
    const newA = document.createElement('a');
    newA.href = '#';
    newA.id = 'item-' + text;
    newA.text = text;
    newA.setAttribute('onclick', 'select(this)');

    document.getElementById('stationSelection').appendChild(newA);
  }

  drawMap(departureStation) {
    const baseStation = this.baseStations.get(departureStation);
    this.map = new google.maps.Map(document.getElementById('map'), {
            center: {lat: baseStation.lat || 47.50022955, lng: baseStation.lon || 19.08387200},
            zoom: 9,
            controlSize: 24
          });
    let minimumMinute = '9999';

    fetch(this.baseUrl + 'destinationsForMap/' + baseStation.id)
          .then(response => response.json())
          .then(data => {
              data.forEach(function(item) {
                  this.addMarker({lat: item.lat, lng: item.lon}, item);
                  if (parseInt(minimumMinute, 10) > parseInt(item.minutes, 10)) {
                    console.log('minimum: ' + item.minutes);
                    minimumMinute = item.minutes;
                  }
              });
              document.getElementById('minutesRange').min = minimumMinute;
              this.hideMarkers();
          });
}

addMarker(latLng, item) {
  let url = 'https://maps.google.com/mapfiles/ms/icons/';
  url += item.color + '-dot.png';

  const marker = new google.maps.Marker({
    visible: false,
    map: this.map,
    position: latLng,
    icon: {
      url: url
    },
    animation: google.maps.Animation.DROP,
    label: item.minutes,
    url: item.elviraUrl
  });

  var infowindow = new google.maps.InfoWindow();
  google.maps.event.addListener(marker, 'mouseover', function() {
       infowindow.setContent(item.departure + ' - ' + item.destination + ' ' + item.minutes + ' perc');
       infowindow.open(this.map, marker);
     });
  google.maps.event.addListener(marker, 'mouseout', function() {
       infowindow.close();
     });

  google.maps.event.addListener(marker, 'click', function() {
    const win = window.open(item.elviraUrl, '_blank');
    win.focus();
  });

  this.markersArray.push(marker);
}

hideMarkers() {
    const minutes = parseInt(document.getElementById('minutesRange').innerText, 10);
    console.log('minutesRange: ' + minutes);
    this.markersArray.forEach((marker) => {
        marker.setVisible(parseInt(marker.label, 10) <= minutes);
    });
}
}
