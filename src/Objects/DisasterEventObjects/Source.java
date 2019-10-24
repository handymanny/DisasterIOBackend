package Objects.DisasterEventObjects;

import com.google.gson.annotations.SerializedName;

public class Source {

    // Field Variables
    @SerializedName("id")
    private String sourceId;
    @SerializedName("url")
    private String sourceUrl;

    // Constructor
    public Source(String sourceId, String sourceUrl) {
        this.sourceId = sourceId;
        this.sourceUrl = sourceUrl;
    }

    public Source() {
        // Default Constructor
    }

    // Getters & Setters
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Override
    public String toString() {
        return "Source{" +
                "sourceId='" + sourceId + '\'' +
                ", sourceUrl='" + sourceUrl + '\'' +
                '}';
    }
}
