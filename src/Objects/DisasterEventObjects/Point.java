package Objects.DisasterEventObjects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Point extends Coordinate {

    // Field Variables
    @SerializedName("date")
    private String coordinateDate;
    @SerializedName("type")
    private String coordinateType;
    @SerializedName("points")
    private ArrayList<Float> points;

    // Constructor
    public Point(String coordinateDate, String coordinateType, String coordinateDate1, String coordinateType1, ArrayList<Float> points) {
        super(coordinateDate, coordinateType);
        this.coordinateDate = coordinateDate1;
        this.coordinateType = coordinateType1;
        this.points = points;
    }

    public Point(String coordinateDate, String coordinateType, ArrayList<Float> points) {
        this.coordinateDate = coordinateDate;
        this.coordinateType = coordinateType;
        this.points = points;
    }

    public Point() {
        // Default Constructor
    }

    // Getters & Setters
    public ArrayList<Float> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Float> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Point{" +
                "coordinateDate='" + coordinateDate + '\'' +
                ", coordinateType='" + coordinateType + '\'' +
                ", points=" + points +
                '}';
    }
}
