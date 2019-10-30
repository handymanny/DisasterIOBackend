package Api.Endpoints;

import Api.EndpointBuilder;
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
        spark.get(basePath + "/crowd/event", (req, res) -> crowdService.getCrowdEvent(), gson::toJson);
    }
}
