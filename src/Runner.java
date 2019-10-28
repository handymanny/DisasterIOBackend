
import Api.ApiContext;
import Api.Endpoints.NasaEventEndpoint;
import Api.Endpoints.SmsEventEndpoint;
import Services.NasaService;
import Services.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Runner {

    // Variables
    private static final Logger log = LoggerFactory.getLogger(Runner.class);


    public static void main (String [] args) {
        ApiContext context = new ApiContext(8080, "/v1/api");
        context.addEndpoint(new NasaEventEndpoint(new NasaService()));
        context.addEndpoint(new SmsEventEndpoint(new SmsService()));
        context.enableCors();
    }
}
