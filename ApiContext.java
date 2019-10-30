package Api;

import WebSockets.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;

public class ApiContext {

    private static final Logger logger = LoggerFactory.getLogger(ApiContext.class);
    private final Service spark;
    private final String basePath;

    public ApiContext(int port, String basePath) {
        this.basePath = basePath;
        spark = Service.ignite().port(port); // import spark.Service;
    }

    public void addEndpoint(EndpointBuilder endpoint) {
        endpoint.configure(spark, basePath);
        logger.info("REST endpoints registered for {}.", endpoint.getClass().getSimpleName());
    }

    public void addWebsockets() {
        spark.webSocket(basePath+"/realtime", WebSocketHandler.class);
    }

    // Then you can even have some fun:
    public void enableCors() {
        spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization");
            response.header("accept", "application/json");
            response.header("Content-Type", "application/json");
        });
        logger.info("CORS support enabled.");
    }
}
