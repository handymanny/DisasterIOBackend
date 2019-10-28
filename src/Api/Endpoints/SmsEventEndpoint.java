package Api.Endpoints;

import Api.EndpointBuilder;
import Objects.MessageSms;
import Services.SmsService;
import com.google.gson.Gson;
import com.nexmo.client.incoming.MessageEvent;
import spark.Service;

public class SmsEventEndpoint implements EndpointBuilder {

    // Service
    private final SmsService smsService;

    // Constructor
    public SmsEventEndpoint(SmsService smsService) {
        this.smsService = smsService;
    }

    @Override
    public void configure(Service spark, String basePath) {
        Gson gson = new Gson();

        // Check if get request
        spark.get(basePath + "/sms/inbound", (req, res) -> smsService.getSmsInbound(
                new MessageSms(req.queryParams("msisdn"),
                req.queryParams("messageId"), req.queryParams("text"),
                req.queryParams("type"), req.queryParams("keyword"),
                req.queryParams("message-timestamp"))), gson::toJson);

        // Check if post request
        spark.post(basePath + "/sms/inbound", (req, res) -> {

            // Check if content is encoded
            if (req.contentType().startsWith("application/x-www-form-urlencoded")) {

                // Return new MessageSms pass it to the service
                return smsService.getSmsInbound(new MessageSms(req.queryParams("msisdn"),
                                      req.queryParams("messageId"),
                                      req.queryParams("text"),
                                      req.queryParams("type"),
                                      req.queryParams("keyword"),
                                      req.queryParams("message-timestamp")));
            } else {
                MessageEvent event = MessageEvent.fromJson(req.body());

                // Return new MessageSms pass it to the service
                return smsService.getSmsInbound(new MessageSms(event.getMsisdn(),
                                      event.getMessageId(),
                                      event.getText(),
                                      event.getType().name(),
                                      event.getKeyword(),
                                      event.getMessageTimestamp().toString()));
            }

        }, gson::toJson);
    }
}

