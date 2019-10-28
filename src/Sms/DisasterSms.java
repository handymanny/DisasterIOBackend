package Sms;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.sms.MessageStatus;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.messages.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisasterSms {

    // Field Variables
    private final String ACCOUNT_KEY = "1c9fff6f";
    private final String ACCOUNT_SECRET = "ROQ2ESnv0NsLpRAZ";
    private final String COMPANY_NAME = "14166194538";

    // Others
    private Logger log;
    private NexmoClient client;
    private String toNumber;
    private String message;

    // Constructors
    public DisasterSms (String toNumber, String message) {
        // Setup client
        client = NexmoClient.builder().apiKey(ACCOUNT_KEY).apiSecret(ACCOUNT_SECRET).build();
        log = LoggerFactory.getLogger(DisasterSms.class);

        // Initialize Variables
        this.toNumber = toNumber;
        this.message = message;
    }

    // Getters & Setters
    public String getToNumber() {
        return toNumber;
    }

    public void setToNumber(String TO_NUMBER) {
        this.toNumber = TO_NUMBER;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Methods
    public boolean sendMessage () {

        // Send a text message to a person
        TextMessage textMessage = new TextMessage(COMPANY_NAME, toNumber, message);

        // Send text message
        SmsSubmissionResponse response = client.getSmsClient().submitMessage(textMessage);

        // Check messsage
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message Sent!");
            return true;
        } else {
            log.error("Message failed with error: "+response.getMessages().get(0).getErrorText());
            return false;
        }
    }
}
