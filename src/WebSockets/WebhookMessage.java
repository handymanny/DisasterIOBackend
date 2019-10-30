package WebSockets;

import com.google.gson.annotations.SerializedName;

public class WebhookMessage {

    // Field Variables
    @SerializedName("method")
    private int messageType;

    @SerializedName("data")
    private Object data;

    // Constructor
    public WebhookMessage(int messageType, Object data) {
        this.messageType = messageType;
        this.data = data;
    }

    // Getters & Setters
    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Object getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
