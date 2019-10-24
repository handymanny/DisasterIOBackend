package Objects.DisasterEventObjects;

import com.google.gson.annotations.SerializedName;

public class Coordinate {

    // Field Variables
    private String coordinateDate;
    private String coordinateType;

    // Constructor
    public Coordinate(String coordinateDate, String coordinateType) {
        this.coordinateDate = coordinateDate;
        this.coordinateType = coordinateType;
    }

    public Coordinate() {
        // Default Constructor
    }

    // Getters & Setters
    public String getCoordinateDate() {
        return coordinateDate;
    }

    public void setCoordinateDate(String coordinateDate) {
        this.coordinateDate = coordinateDate;
    }

    public String getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(String coordinateType) {
        this.coordinateType = coordinateType;
    }
}
