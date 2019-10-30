mapboxgl.accessToken = 'pk.eyJ1IjoiZGlzYXN0ZXJpbyIsImEiOiJjazF3eXpuaDgwNzMzM2x1YXA2YzViOGFwIn0.r_CUebFU80v4DrNZLTL1Tw';
let polyList = [];
let dataList = [];

// Setup websocket
let socket = new WebSocket("ws://localhost:8080/v1/api/realtime");

const map = new mapboxgl.Map({
    container: 'map', // container id
    style: 'mapbox://styles/mapbox/streets-v11', // stylesheet location
    center: [127.842865, 1.6633016], // starting position [lng, lat]
    zoom: 12 // starting zoom
});

const geocoder = new MapboxGeocoder({
    accessToken: mapboxgl.accessToken,
    marker: {
        color: 'orange'
    },
    mapboxgl: mapboxgl
});

map.addControl(geocoder);

// Add geolocate control to the map.
map.addControl(new mapboxgl.GeolocateControl({
    positionOptions: {
        enableHighAccuracy: true
    },
    trackUserLocation: true
}));

$(document).ready(function () {

    $.getJSON("http://localhost:8080/v1/api/nasa", ParseNasaEventData);

    // Let user know they are connected to socket
    socket.onopen = function(e) {
        console.log("Real-time socket connected");
    };

    // Parse data when message is retrieved
    socket.onmessage = function(event) {
        let data = JSON.parse(event.data);
        console.log("Data received: "+data);

        addCrowdMarker(data);

    };

    // Let user know when socket is closed
    socket.onclose = function(event) {
        if (event.wasClean) {
            console.log("Connection closed cleanly");
        } else {
            // e.g. server process killed or network down
            // event.code is usually 1006 in this case
            console.log("Connection died");
        }
    };

    // Let user know when there is an error on the socket
    socket.onerror = function(error) {
        alert(`Error: ${error.message}`);
    };

    map.on('load', function () {

    });

    function ParseNasaEventData (data) {
        dataList = data;
        // Parse Data coordinates
        for (let i = 0; i < data.length; i++) {

            // Get id, link description, title
            var eventId = data[i].id;
            var eventLink = data[i].link;
            var eventDescription = data[i].description;
            var eventTitle = data[i].title;

            // Get Categories, source, geometries
            var eventCategories = data[i].categories;
            var eventSources = data[i].sources;
            var eventGeo;

            // Image
            var imageName = getImageFromTitle(eventCategories[0].title);

            // Check type of vector
            if (data[i].points != null) {
                eventGeo = data[i].points;
            } else {
                eventGeo = data[i].polygons;

                map.addLayer({
                    'id': eventId,
                    'type': 'fill',
                    'source': {
                        'type': 'geojson',
                        'data': {
                            'type': 'Feature',
                            'geometry': {
                                'type': 'Polygon',
                                'coordinates': convertPointToGeo(eventGeo)
                            }
                        }
                    },
                    'layout': {
                        'visibility': 'visible'
                    },
                    'paint': {
                        'fill-color': '#088',
                        'fill-opacity': 0.8
                    }
                });
            }

            // Create marker object
            // create a HTML element for each feature
            let el = document.createElement('div');
            el.className = 'marker';
            el.id = eventId;

            // Create IMG Tag
            let er = document.createElement('img');
            er.className = 'marker-img';
            er.src = imageName;
            er.width = "40px;";
            er.height = "40px;";

            // Spread Lay
            let spr = document.createElement('div');
            spr.className = 'spread';
            spr.id = eventId +"SPREAD";

            // Append Img to DIV
            el.appendChild(er);

            // Create Marker on the map
                for (let x = 0; x < eventGeo.length; x++) {

                    if (eventGeo[x].type === "Point") {
                        new mapboxgl.Marker(spr)
                            .setLngLat(eventGeo[x].points)
                            .addTo(map);
                        new mapboxgl.Marker(el)
                            .setLngLat(eventGeo[x].points)
                            .addTo(map);

                    }

                }

        }

    }

    function getImageFromTitle(title) {
        // Image names
        let images = ["dust-and-haze", "icebergs", "manmade", "sea-and-lake-ice", "severe-storms", "snow", "volcanoes", "water-color", "wildfires"]
        let temp = "";

        images.forEach(ele => {
            title = title.split(" ").join("-").toLowerCase();
            if (title === ele) {
                temp = "./Res/icon-" + ele + ".svg";
            }
        });

        return temp;
    }

    function convertPointToGeo (event) {
        let temp = [[]];

        for (let i = 0; i < event.length; i++) {
            temp[0].push(event[i].points);
        }

        return temp;
    }

});


function addCrowdMarker(event) {
         new mapboxgl.Marker()
                    .setLngLat(event.geometry.coordinates)
                    .addTo(map);

}

function testCrowdEvent (msg) {
    $.ajax({
        url: "http://localhost:8080/v1/api/crowd/event"
    });
}