package Api.Endpoints;

import Api.EndpointBuilder;
import Objects.MarkerMessage;
import Services.CrowdService;
import com.google.gson.Gson;
import spark.Service;

public class CrowdEventEndpoint implements EndpointBuilder {

    // Service
    private CrowdService crowdService;

    public CrowdEventEndpoint(CrowdService service) {
        this.crowdService = service;
    }

    @Override
    public void configure(Service spark, String basePath) {
        Gson gson = new Gson();
        spark.get(basePath + "/crowd/event", (req, res) -> {

            float cord_lat = Float.valueOf(req.queryParams("lat"));
            float cord_lng = Float.valueOf(req.queryParams("lng"));
            String cord_event = req.queryParams("event");

            MarkerMessage mkMsg = new MarkerMessage(cord_lat, cord_lng, cord_event);

            return crowdService.getCrowdEvent(mkMsg);
        }, gson::toJson);
    }
}
