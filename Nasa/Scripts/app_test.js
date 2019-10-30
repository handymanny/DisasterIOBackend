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
        try {
            let data = JSON.parse(event.data);

            // Message Type
            if (data.method === 1) {
                addSmsMarker(data)

            } else if (data.method === 2) {
                addCrowdMarker(data)
            }

        } catch(err) {
            console.log("Not a JSON Object");
        }

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
        console.log(`Error: ${error.message}`);
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


    $(".file-upload").on('change', function(){
        readURL(this);
    });

    $("#report-btn").click(()=> {
        $("#modal-select").removeAttr("hidden");
    });

    $("#report1-submit").click(() => {
        let text = highlighted.children("p").text();
        $("#modal-reportfor").text(`Report for ${text}`);
        $("#modal-select").attr("hidden", true);
        $("#modal-report").removeAttr("hidden");
    });

    $("#report1-close").click(() => {
        $("#modal-select").attr("hidden", true);
    });

    $("#report-submit").click(()=> {
        $("#modal-report").attr("hidden", true);
    });

    $("#report-close").click(()=> {
        $("#modal-report").attr("hidden", true);
    });


    let highlighted;

    $(".highlight-on-click").click(function() {

        // remove highlight on click class
        // add highlight on click class to current
        // store reference to currently highlighted

        if (highlighted !== undefined) {
            highlighted.toggleClass("highlight");
        }

        $(this).toggleClass("highlight");
        highlighted = $(this);
    });




//    Crowd Markers on map based on center of the screen
    $("#report-submit").on('click', function () {

        // Check for highlighted item
        let x = document.getElementsByClassName("highlight-on-click highlight")[0];
        let imageName = x.getAttribute('value');

        let cord_f = map.getCenter();
        let cord = { lng : cord_f['lng'], lat : cord_f['lat']};

        // Update other users
        sendCrowdEvent({cord, imageName});
        console.log("New Marker Created");
    });

    function addCrowdMarker (msg) {
        console.log(msg);

        // Create style of marker
        let el = document.createElement('div');
        el.className = 'marker';
        el.id = "CROWD"+Date.now();

        let coords = [msg.data.lng, msg.data.lat];

        let er = document.createElement('img');
        er.className = 'marker-img';
        er.src = msg.data.image_name;
        er.width = "40px;";
        er.height = "40px;";

        el.appendChild(er);

        // Set marker on center
        new mapboxgl.Marker(el)
            .setLngLat(coords)
            .addTo(map);


        console.log("New Crowd Marker Created");
    }

});


function addSmsMarker(event) {
    eventData = JSON.parse(event.data);

    console.log(eventData);
         new mapboxgl.Marker()
                    .setLngLat(eventData.geometry.coordinates)
                    .addTo(map);

}

function sendCrowdEvent (msg) {
    $.ajax({
        url: "http://localhost:8080/v1/api/crowd/event",
        data: {'lng' : msg.cord.lng, 'lat' : msg.cord.lat, 'event' : msg.imageName}
    });
}
