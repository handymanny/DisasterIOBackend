package Objects.DisasterEventObjects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

public class Polygon extends Coordinate {

    // Field Variables
    @SerializedName("date")
    private String coordinateDate;
    @SerializedName("type")
    private String coordinateType;
    @SerializedName("points")
    private Map<Integer, ArrayList<Float>> points;

    // Constructor
    public Polygon(String coordinateDate, String coordinateType, String coordinateDate1, String coordinateType1, Map<Integer, ArrayList<Float>> points) {
        super(coordinateDate, coordinateType);
        this.coordinateDate = coordinateDate1;
        this.coordinateType = coordinateType1;
        this.points = points;
    }

    public Polygon(String coordinateDate, String coordinateType, Map<Integer, ArrayList<Float>> points) {
        this.coordinateDate = coordinateDate;
        this.coordinateType = coordinateType;
        this.points = points;
    }

    public Polygon () {
        // Default Constructor
    }

    // Getter & Setters

    public Map<Integer, ArrayList<Float>> getPoints() {
        return points;
    }

    public void setPoints(Map<Integer, ArrayList<Float>> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "coordinateDate='" + coordinateDate + '\'' +
                ", coordinateType='" + coordinateType + '\'' +
                ", points=" + points +
                '}';
    }
}
