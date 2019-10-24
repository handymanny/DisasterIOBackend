mapboxgl.accessToken = 'pk.eyJ1IjoiZGlzYXN0ZXJpbyIsImEiOiJjazF3eXpuaDgwNzMzM2x1YXA2YzViOGFwIn0.r_CUebFU80v4DrNZLTL1Tw';
var dataList = [];

const map = new mapboxgl.Map({
    container: 'map', // container id
    style: 'mapbox://styles/mapbox/streets-v11', // stylesheet location
    center: [0, 0], // starting position [lng, lat]
    zoom: 4 // starting zoom
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


    map.on('load', function () {


    });

    function ParseNasaEventData (data) {
        dataList = data;
        // Parse Data coordinates
        for (let i = 0; i < data.length; i++) {

            // Get id, link description, title
            let eventId = data[i].id;
            let eventLink = data[i].link;
            let eventDescription = data[i].description;
            let eventTitle = data[i].title;

            // Get Categories, source, geometries
            let eventCategories = data[i].categories;
            let eventSources = data[i].sources;
            let eventGeo;

            // Image
            let imageName = getImageFromTitle(eventCategories[0].title);

            // Check type of vector
            try {
                eventGeo = data[i].points;
            } catch (e) {
                eventGeo = data[i].polygon;
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


});