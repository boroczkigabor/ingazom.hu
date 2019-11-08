let map;
let markersArray = [];

    function initMap() {
      map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 47.50022955, lng: 19.08387200},
        zoom: 9
      });

      fetch('http://commuter.com:8080/destinationsForMap')
              .then(response=>response.json())
              .then(data => {
                  data.forEach(function(item) {
                      addMarker({lat: item.lat, lng: item.lon}, item.color, item.name, item.minutes)
                  })
              })
    }

    function addMarker(latLng, color, title, minutes = '0') {
      let url = "http://maps.google.com/mapfiles/ms/icons/";
      url += color + "-dot.png";

      let marker = new google.maps.Marker({
        map: map,
        position: latLng,
        icon: {
          url: url
        },
        animation: google.maps.Animation.DROP,
        title: title,
        label: minutes
      });

      //store the marker object drawn in global array
      markersArray.push(marker);
    }