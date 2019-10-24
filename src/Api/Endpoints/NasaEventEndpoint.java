package Api.Endpoints;

import Api.EndpointBuilder;
import Services.NasaService;
import com.google.gson.Gson;
import spark.Service;

public class NasaEventEndpoint implements EndpointBuilder {

    // Service
    private final NasaService nasaService;

    // Constructor
    public NasaEventEndpoint(NasaService nasaService) {
        this.nasaService = nasaService;
    }

    @Override
    public void configure(Service spark, String basePath) {
        Gson gson = new Gson();
        spark.get(basePath + "/nasa", (req, res) -> nasaService.getNasaEventData(), gson::toJson);
    }
}
