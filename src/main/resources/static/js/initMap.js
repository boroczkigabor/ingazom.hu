let map;
let markersArray = [];

    function initMap() {
      map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 47.50022955, lng: 19.08387200},
        zoom: 9,
        controlSize: 24
      });
      let departureStation = 'BUDAPEST*' // TODO dropdown for this

      fetch('/destinationsForMap/' + departureStation)
              .then(response=>response.json())
              .then(data => {
                  data.forEach(function(item) {
                      addMarker({lat: item.lat, lng: item.lon}, item.color, item.name, item.minutes, item.elviraUrl)
                  })
              })
    }

    function addMarker(latLng, color, title, minutes = '0', elviraUrl) {
      let url = "http://maps.google.com/mapfiles/ms/icons/";
      url += color + "-dot.png";

      let marker = new google.maps.Marker({
        map: map,
        position: latLng,
        icon: {
          url: url
        },
        animation: google.maps.Animation.DROP,
        label: minutes,
        url: elviraUrl,
        visible: false
      });

      var infowindow = new google.maps.InfoWindow();
      google.maps.event.addListener(marker, 'mouseover', function() {
           infowindow.setContent(title + ' - ' + minutes + ' perc');
           infowindow.open(map, marker);
         });
      google.maps.event.addListener(marker, 'mouseout', function() {
           infowindow.close();
         });

      google.maps.event.addListener(marker, 'click', function() {
        var win = window.open(elviraUrl, '_blank');
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

    window.onload = function() {
        hideMarkers();
    };