package DataController;

import Objects.MessageSms;
import Sms.DisasterSms;
import WebSockets.WebSocketHandler;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;

public class SmsController {

    // Field Variables
    private DisasterSms sender;
    private final String ADDR = "15192173146";

    // Constructor
    public SmsController () {
        //Default
    }

    // Methods
    public boolean getSmsInbound(MessageSms sms) {
        //Create new DisasterSms Object
        if (sms != null || sms.getMsisdn() != null) {

            // Temporary
            String address;

            // Configure marker to send
            if (sms.getText().startsWith("Disaster")) {
                address = sms.getText().substring(8);

                // Responder
                sender = new DisasterSms(ADDR, "Thank you for the update! We have updated "+address);

                // Send our address to api
                MapBoxController.getGeocoderResult(GeocodingCriteria.TYPE_ADDRESS, address);

            }

            // Send our message and return our result
            return sender.sendMessage();
        } else {
            return false;
        }
    }
}
