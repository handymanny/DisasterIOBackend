package Objects;

import com.google.gson.annotations.SerializedName;

public class MarkerMessage {

    // Field Variables
    @SerializedName("lat")
    private float lat;

    @SerializedName("lng")
    private float lng;

    @SerializedName("image_name")
    private String imageName;


    // Constructor
    public MarkerMessage(float lat, float lng, String imageName) {
        this.lat = lat;
        this.lng = lng;
        this.imageName = imageName;
    }

    // Getters & Setters

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
