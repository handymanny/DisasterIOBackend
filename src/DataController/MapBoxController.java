package DataController;

import WebSockets.WebSocketHandler;
import WebSockets.WebhookMessage;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MapBoxController {
    // Final
    private static final String accessToken = "pk.eyJ1IjoiZGlzYXN0ZXJpbyIsImEiOiJjazF3eXpuaDgwNzMzM2x1YXA2YzViOGFwIn0.r_CUebFU80v4DrNZLTL1Tw";
    private static final Logger log = LoggerFactory.getLogger(MapBoxController.class);

    // Methods
    public static void getGeocoderResult(String type, String query) {

        // Create geocode object
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(accessToken)
                .geocodingTypes(type)
                .query(query)
                .build();

        // Run request
        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                List<CarmenFeature> results = response.body().features();

                if (results.size() > 0) {

                    // Log the first results Point.
                    WebSocketHandler.broadcast(new WebhookMessage(1, results.get(0).toJson()));

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
    }

}
