package DataAccess;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class NasaDataAccess {

    // Field Variables
    private final String baseUrl = "https://eonet.sci.gsfc.nasa.gov/api/v2.1/events";

    // HTTP Method Calls
    public HttpResponse<JsonNode> getNasaEventData () {
        // Get event data from nasa events
        return Unirest.get(baseUrl)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .asJson();
    }

    // Call to get another layer ++
}
