package Services;

import DataController.SmsController;
import Objects.MessageSms;

public class SmsService {

    // Sms Controller
    private SmsController smsController;

    // Contructor
    public SmsService () {
        // Default
        smsController = new SmsController();
    }

    // Methods
    public boolean getSmsInbound(MessageSms msg) {
        return smsController.getSmsInbound(msg);
    }
}
