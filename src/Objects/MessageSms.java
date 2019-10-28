package Objects;

public class MessageSms {

    // Field Variables
    private String msisdn;
    private String messageId;
    private String text;
    private String type;
    private String keyword;
    private String timestamp;

    // Constructor

    public MessageSms(String msisdn, String messageId, String text, String type, String keyword, String timestamp) {
        this.msisdn = msisdn;
        this.messageId = messageId;
        this.text = text;
        this.type = type;
        this.keyword = keyword;
        this.timestamp = timestamp;
    }


    // Methods

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
