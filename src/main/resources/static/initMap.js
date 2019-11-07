let map;
let markersArray = [];

    function initMap() {
      map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 47.50022955, lng: 19.08387200},
        zoom: 10
      });

      addMarker({lat: 47.50022955, lng: 19.08387200}, "blue", "Budapest Keleti");
      addMarker({lat: 47.43895881, lng: 19.34071599}, "green", "Maglód");
      addMarker({lat: 47.48431003, lng: 18.61845608}, "yellow", "Bicske");

      fetch('http://commuter.com:8080/routes')
        .then(response=>response.json())
        .then(data=>{ console.log(data);})
    }

    function addMarker(latLng, color, title, minutes='0') {
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