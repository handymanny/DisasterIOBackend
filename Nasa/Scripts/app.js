
// Global Variables
mapboxgl.accessToken = 'pk.eyJ1IjoiZGlzYXN0ZXJpbyIsImEiOiJjazF3eXpuaDgwNzMzM2x1YXA2YzViOGFwIn0.r_CUebFU80v4DrNZLTL1Tw';
var dataList = [];
var disasterChoice = "";

// Variables
var truckLocation = [-106.10412741596872, 38.46706263084121];
var warehouseLocation = [-106.05924210401884, 39.63116809403496];
var lastQueryTime = 0;
var lastAtRestaurant =0;
var keepTrack =[];
var currentSchedule = [];
var currentRoute = null;
var pointHopper = {};
var pause = true;
var speedFactor = 50;


// Word List
var wordList = ["arson", "arsonist", "avalanche", "barometer", "Beaufortscale", "blackout", "blizzard"
    , "blow", "cloud", "crust", "cumulonimbus", "cyclone", "dam", "drought", "duststorm", "earthquake", "erosion"
    , "fatal", "fault", "fire", "flood", "fog", "force", "forest", "forestfire", "gale", "geyser", "gust", "hail"
    , "hailstorm", "heat", "high-pressure", "hurricane", "iceberg", "kamikaze", "lack", "lava", "lightning", "low-pressure"
    , "magma", "mountain", "nimbus", "ocean", "permafrost", "rain", "rainstorm", "Richterscale", "river", "sandstorm"
    , "sea", "seismic", "sinking", "snowstorm", "storm", "stuck", "thunderstorm", "tornado", "tsunami", "twister"
    , "violentstorm", "volcano", "volt", "whirlpool", "whirlwind", "windscale", "windvane", "windstorm", "heatwave"
    , "wave", "tremor", "underground", "death", "casualty", "fatality", "disaster", "money", "lost", "damage", "life"
    , "poor", "shelter", "rescue", "coast", "monster", "myth", "science", "scientist", "god", "goddess", "sink", "boat"
    , "destruction", "destroy", "uproot", "tree", "fate", "poverty", "impoverish", "farm", "touchdown", "zap", "tension"
    , "nightmare", "monstrosity", "oil", "spill", "cataclysm", "Bermuda", "BermudaTriangle", "wind", "windy", "wave"
    , "ice", "transport"];

const map = new mapboxgl.Map({
    container: 'map', // container id
    style: 'mapbox://styles/mapbox/streets-v11', // stylesheet location
    center: truckLocation, // starting position [lng, lat]
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

// Turf Data
var warehouse = turf.featureCollection([turf.point(warehouseLocation)]);
var dropoffs = turf.featureCollection([]);
var nothing = turf.featureCollection([]);


$(document).ready(function () {


    map.on('load', function () {

        $.getJSON("https://eonet.sci.gsfc.nasa.gov/api/v2.1/events", getData);

        var marker = document.createElement('div');
        marker.classList = 'truck';

        // Create a new marker
        truckMarker = new mapboxgl.Marker(marker)
            .setLngLat(truckLocation)
            .addTo(map);

        // Create a circle layer
        map.addLayer({
            id: 'warehouse',
            type: 'circle',
            source: {
                data: warehouse,
                type: 'geojson'
            },
            paint: {
                'circle-radius': 20,
                'circle-color': 'white',
                'circle-stroke-color': '#3887be',
                'circle-stroke-width': 3
            }
        });

        // Create a symbol layer on top of circle layer
        map.addLayer({
            id: 'warehouse-symbol',
            type: 'symbol',
            source: {
                data: screen,
                type: 'geojson'
            },
            layout: {
                'icon-image': 'grocery-15',
                'icon-size': 1
            },
            paint: {
                'text-color': '#3887be'
            }
        });

        map.addLayer({
            id: 'dropoffs-symbol',
            type: 'symbol',
            source: {
                data: dropoffs,
                type: 'geojson'
            },
            layout: {
                'icon-allow-overlap': true,
                'icon-ignore-placement': true,
                'icon-image': 'marker-15',
            }
        });

        map.addSource('route', {
            type: 'geojson',
            data: nothing
        });

        map.addLayer({
            id:'routeline-active',
            type:'line',
            source: 'route',
            layout:{
                'line-join': 'round',
                'line-cap': 'round'
            },
            paint:{
                'line-color': '#3887be',
                'line-width': [
                    "interpolate",
                    ["linear"],
                    ["zoom"],
                    12, 3,
                    22, 12
                ]
            }
        }, 'waterway-label');

        map.addLayer({
            id: 'routearrows',
            type: 'symbol',
            source: 'route',
            layout: {
                'symbol-placement': 'line',
                'text-field': '▶',
                'text-size': [
                    "interpolate",
                    ["linear"],
                    ["zoom"],
                    12, 24,
                    22, 60
                ],
                'symbol-spacing': [
                    "interpolate",
                    ["linear"],
                    ["zoom"],
                    12, 30,
                    22, 160
                ],
                'text-keep-upright': false
            },
            paint: {
                'text-color': '#3887be',
                'text-halo-color': 'hsl(55, 11%, 96%)',
                'text-halo-width': 3
            }
        }, 'waterway-label');

        // Listen for a click on the map
        map.on('click', function(e) {
            // When the map is clicked, add a new drop off point
            // and update the `dropoffs-symbol` layer
            newDropoff(map.unproject(e.point));
            updateDropoffs(dropoffs);
        });
    });

    function newDropoff(coords) {
        // Store the clicked point as a new GeoJSON feature with
        // two properties: `orderTime` and `key`
        var pt = turf.point(
            [coords.lng, coords.lat],
            {
                orderTime: Date.now(),
                key: Math.random()
            }
        );
        dropoffs.features.push(pt);
        pointHopper[pt.properties.key] = pt;

        // Make a request to the Optimization API
        $.ajax({
            method: 'GET',
            url: assembleQueryURL(),
        }).done(function(data) {

            // Create a GeoJSON feature collection
            var routeGeoJSON = turf.featureCollection([turf.feature(data.trips[0].geometry)]);

            // If there is no route provided, reset
            if (!data.trips[0]) {
                routeGeoJSON = nothing;
            } else {
                // Update the `route` source by getting the route source
                // and setting the data equal to routeGeoJSON
                map.getSource('route')
                    .setData(routeGeoJSON);
            }

            //
            if (data.waypoints.length === 12) {
                window.alert('Maximum number of points reached. Read more at docs.mapbox.com/api/navigation/#optimization.');
            }
        });
    }

    function updateDropoffs(geojson) {
        map.getSource('dropoffs-symbol')
            .setData(geojson);
    }

    // Here you'll specify all the parameters necessary for requesting a response from the Optimization API
    function assembleQueryURL() {

        // Store the location of the truck in a variable called coordinates
        var coordinates = [truckLocation];
        var distributions = [];
        keepTrack = [truckLocation];

        // Create an array of GeoJSON feature collections for each point
        var restJobs = objectToArray(pointHopper);

        // If there are actually orders from this restaurant
        if (restJobs.length > 0) {

            // Check to see if the request was made after visiting the restaurant
            var needToPickUp = restJobs.filter(function(d, i) {
                return d.properties.orderTime > lastAtRestaurant;
            }).length > 0;

            // If the request was made after picking up from the restaurant,
            // Add the restaurant as an additional stop
            if (needToPickUp) {
                var restaurantIndex = coordinates.length;
                // Add the restaurant as a coordinate
                coordinates.push(warehouseLocation);
                // push the restaurant itself into the array
                keepTrack.push(pointHopper.warehouse);
            }

            restJobs.forEach(function(d, i) {
                // Add dropoff to list
                keepTrack.push(d);
                coordinates.push(d.geometry.coordinates);
                // if order not yet picked up, add a reroute
                if (needToPickUp && d.properties.orderTime > lastAtRestaurant) {
                    distributions.push(restaurantIndex + ',' + (coordinates.length - 1));
                }
            });
        }

        // Set the profile to `driving`
        // Coordinates will include the current location of the truck,
        return 'https://api.mapbox.com/optimized-trips/v1/mapbox/driving/' + coordinates.join(';') + '?distributions=' + distributions.join(';') + '&overview=full&steps=true&geometries=geojson&source=first&access_token=' + mapboxgl.accessToken;
    }

    function objectToArray(obj) {
        var keys = Object.keys(obj);
        var routeGeoJSON = keys.map(function(key) {
            return obj[key];
        });
        return routeGeoJSON;
    }


    function getData(data) {

        // Get each event into array
        let arr = data.events;
        dataList = arr;

        // Parse each object into coordinated and click elements
        arr.forEach(ele => {

            //Get properties
            let id = ele.id;
            let title = ele.title;
            let description = ele.description;
            let link = ele.link;
            let imageName = getImageFromTitle(ele.categories[0].title);

            // create a HTML element for each feature
            let el = document.createElement('div');
            el.className = 'marker';
            el.id = id;

            // Create IMG Tag
            let er = document.createElement('img');
            er.className = 'marker-img';
            er.src = imageName;
            er.width = "40px;";
            er.height = "40px;";

            // Append Img to DIV
            el.appendChild(er);

            // Create tooltip tag for each marker with information
            $(function() {
                $(er).tooltip({
                    items: er,
                    content: `${title}`,
                    show: "fold",
                    close: function(event, ui) {
                        ui.tooltip.click(function() {
                                $(this).stop(true).fadeTo(500, 1);
                            },
                            function() {
                                $(this).fadeOut('1200', function() {
                                    $(this).remove();
                                });
                            });
                    }
                });
            });

            // Onclick zoom into the coordinates of the marker
            $(el).on('click', function() {
                map.flyTo({
                    center: getCoords(ele).geometries[0].coordinates,
                    zoom: 12,
                    speed: 1,
                });
            });


            // make a marker for each feature and add to the map
            if (ele.geometries[0].type === "Point") {
                new mapboxgl.Marker(el)
                    .setLngLat(ele.geometries[0].coordinates)
                    .addTo(map);

            }
        });
    }

    $("#icon-layers").on('click', function() {

        // Get overlay
        let overlay = $("#overlay");

        // Check Height
        if (overlay.height() > 100) {
            overlay.height(40);
            $('#icon-layers').css('border-bottom', 'none');
        } else {
            overlay.height(400);
            $('#icon-layers').css('border-bottom', '1px black solid');
        }


    });


    function getCoords (object) {
        // tempory holder
        let holder;

        // Zoom that bitch in
        dataList.forEach(ele => {
            if (ele.id === object.id) {
                holder = ele;
            }
        });

        return holder;
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


        $('#upload-report').on('click', function () {

            // Click input
            $(".file-upload").click();

        });

    function readURL(input) {
        // Click to upload a image
        let apiKey = "AIzaSyBuibd6uAiLx1pvKlHdZrZUQsSqD0zsyU8";
        let rq = "";

        // Checks that we have an input file
        if (input.files && input.files[0]) {

            // Opens new FIle Explorer windows
            var FR= new FileReader();

            // Wait for FR to have an item so we can pull base64 from image
            FR.addEventListener("load", function(e) {
                imageString = e.target.result;
                imageString = imageString.split(",")[1];
                rq = '{\n' +
                    '  "requests":[\n' +
                    '    {\n' +
                    '      "image":{\n' +
                    '        "content":\n' +
                    '        "'+imageString+'"\n' +
                    '      },\n' +
                    '      "features":[\n' +
                    '        {\n' +
                    '          "type":"LABEL_DETECTION",\n' +
                    '          "maxResults":5\n' +
                    '        }\n' +
                    '      ]\n' +
                    '    }\n' +
                    '  ]\n' +
                    '}';

                // Make Request to google vision api
                $.ajax({
                    type: "POST",
                    url: "https://vision.googleapis.com/v1/images:annotate?key="+apiKey,
                    data: rq,
                    success: checkLabels,
                    dataType: "json",
                    contentType: "application/json; charset=utf-8"
                });
            });

            // Function that triggers change in value
            FR.readAsDataURL( input.files[0] );

        }

    }


    // Check that we have a valid disaster
    function isValidDisaster (value) {

        let flag = false;

        wordList.forEach(word => {

            if (word === value.toLowerCase()) {
                flag = true;
            }

        });

        return flag;
    }

    // Make sure the image provided was legit
    function checkLabels(data) {

        // Output
        let wordOne = data.responses[0].labelAnnotations[0].description;
        let wordTwo = data.responses[0].labelAnnotations[1].description;
        let wordThree = data.responses[0].labelAnnotations[2].description;
        console.log(wordOne+" "+wordTwo+" "+wordThree);
        let flag = false;
        
        // Check words
        if (isValidDisaster(wordOne) || isValidDisaster(wordTwo) || isValidDisaster(wordThree)) {
            // Chamge camera Icon
            flag = true;
            $(".camera-icon").attr("src", "./Res/check-mark.png");

        } else {
            // Chamge camera Icon
            flag = false;
            $(".camera-icon").attr("src", "./Res/red-x.png");


        }

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


    var highlighted;

    $(".highlight-on-click").click(function() {

        // remove highlight on click class
        // add highlight on click class to current
        // store reference to currently highlighted

        if (highlighted != undefined) {
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

        console.log(imageName);
        let cord_f = map.getCenter();
        let cord_piss = { lng : cord_f['lng'], lat : cord_f['lat']};

        // Create style of marker
        let el = document.createElement('div');
        el.className = 'marker';
        el.id = "CROWD"+Date.now();

        let er = document.createElement('img');
        er.className = 'marker-img';
        er.src = imageName;
        er.width = "40px;";
        er.height = "40px;";

        el.appendChild(er);

        // Set marker on center
        new mapboxgl.Marker(el)
            .setLngLat(cord_piss)
            .addTo(map);

        console.log("New Marker Created");
    });


});
