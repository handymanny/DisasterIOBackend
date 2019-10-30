package DataController;

import Objects.MarkerMessage;
import WebSockets.WebSocketHandler;
import WebSockets.WebhookMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class CrowdDataController {

    // Field Variables
    private Logger log;

    public CrowdDataController () {
        log = LoggerFactory.getLogger(CrowdDataController.class);
    }

    public String getCrowdEvent(MarkerMessage mkMsg) {

        WebhookMessage message = new WebhookMessage(2, mkMsg);

        WebSocketHandler.broadcast(message);

        return "yes";
    }

}
