package DataController;

import WebSockets.WebSocketHandler;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;


public class CrowdDataController {

    // Field Variables
    private Logger log;

    public CrowdDataController () {
        log = LoggerFactory.getLogger(CrowdDataController.class);
    }

    public String getCrowdEvent() {

        // Create geocode object
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken("pk.eyJ1IjoiZGlzYXN0ZXJpbyIsImEiOiJjazF3eXpuaDgwNzMzM2x1YXA2YzViOGFwIn0.r_CUebFU80v4DrNZLTL1Tw")
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .query("23 Hosking Pl, Guelph ON")
                .build();

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                List<CarmenFeature> results = response.body().features();

                if (results.size() > 0) {

                    // Log the first results Point.
                    WebSocketHandler.broadcast(results.get(0).toJson());

                } else {

                    // No result for your request were found.
                    log.info("onResponse: No result found");

                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        return "yes";
    }

}
