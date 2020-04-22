let map;
let markersArray = [];
let baseStations = new Map();
//let baseUrl = 'http://localhost:5000';
let baseUrl = 'https://ingazom.eu-west-3.elasticbeanstalk.com';

    function initMap() {
      let departureStation = 'BUDAPEST*'; // TODO retrieve from cookie or fallback
      changeDropDownTextTo(departureStation);
      getBaseStations()
        .then(() => drawMap(departureStation));

    }

    function getBaseStations() {
        return fetch(baseUrl + '/baseStations/')
            .then(response => response.json())
            .then(data => {
                data.forEach( function(item) {
                    baseStations.set(item.name, item);
                    addNewSelectionItem(item.name);
                });
            });
    }

    function addNewSelectionItem(text) {
        var newA = document.createElement("a");
        newA.href="#";
        newA.id="item-" + text;
        newA.text=text;
        newA.setAttribute('onclick', 'select(this)');

        document.getElementById('stationSelection').appendChild(newA);
    }

    function drawMap(departureStation) {
        var baseStation = baseStations.get(departureStation);
        map = new google.maps.Map(document.getElementById('map'), {
                center: {lat: baseStation.lat || 47.50022955, lng: baseStation.lon || 19.08387200},
                zoom: 9,
                controlSize: 24
              });
        let minimumMinute = '9999';

        fetch(baseUrl + '/destinationsForMap/' + baseStation.id)
              .then(response=>response.json())
              .then(data => {
                  data.forEach(function(item) {
                      addMarker({lat: item.lat, lng: item.lon}, item);
                      if (parseInt(minimumMinute, 10) > parseInt(item.minutes, 10)) {
                        console.log('minimum: ' + item.minutes);
                        minimumMinute = item.minutes;
                      }
                  });
                  document.getElementById('minutesRange').min = minimumMinute;
                  hideMarkers();
              });
    }

    function addMarker(latLng, item) {
      let url = "https://maps.google.com/mapfiles/ms/icons/";
      url += item.color + "-dot.png";

      let marker = new google.maps.Marker({
        visible: false,
        map: map,
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
           infowindow.open(map, marker);
         });
      google.maps.event.addListener(marker, 'mouseout', function() {
           infowindow.close();
         });

      google.maps.event.addListener(marker, 'click', function() {
        var win = window.open(item.elviraUrl, '_blank');
        win.focus();
      });

      markersArray.push(marker);
    }

    function hideMarkers() {
        var minutes = parseInt(document.getElementById('minutesRange').value, 10);
        console.log('minutesRange: ' + minutes);
        markersArray.forEach(function(marker) {
            marker.setVisible(parseInt(marker.label, 10) <= minutes);
        });
    }
