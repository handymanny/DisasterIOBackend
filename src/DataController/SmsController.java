package DataController;

import Objects.MessageSms;
import Sms.DisasterSms;

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
            sender = new DisasterSms(ADDR, "Thank you for the update!");

            // Send our message and return our result
            return sender.sendMessage();
        } else {
            return false;
        }
    }
}
