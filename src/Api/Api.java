package Api;

import DataController.CrowdDataController;
import DataController.NasaDataController;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class Api {

    // Field Variables
    private NasaDataController nasaDataController;
    private CrowdDataController crowdDataController;
    private Logger log;

    // Constructor
    public Api () {
        // Default Constructor
        init();
    }

    public void init () {
        // Config
        port(8080);

        // Create Objects
        log = LoggerFactory.getLogger(Api.class);
        nasaDataController = new NasaDataController();
        crowdDataController = new CrowdDataController();

        // Endpoints
        setupEndpoints();

        log.info("Started");
    }

    // Endpoints
    private void setupEndpoints () {

        // Transformer
        Gson gson = new Gson();

        path("/v1/api", () -> {
            before("/*", (q, a) -> log.info("Received api call"));
            path("/nasa", () -> {
                get("/",       this::GetNasaEventData);
            });

            path("/crowd", () -> {
                post("/add",       this::AddCrowdMarker);
                delete("/remove",  this::DeleteCrowdMarker);
            });
        });

    }

    // Methods
    private Object GetNasaEventData(Request request, Response response) {
        return nasaDataController.getNasaEventData();
    }

    private Object AddCrowdMarker(Request request, Response response) {
        return "";
    }


    private Object DeleteCrowdMarker(Request request, Response response) {
        return "";
    }


}
