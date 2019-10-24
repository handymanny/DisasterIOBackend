package Objects;

import Objects.DisasterEventObjects.Category;
import Objects.DisasterEventObjects.Point;
import Objects.DisasterEventObjects.Polygon;
import Objects.DisasterEventObjects.Source;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DisasterEvent {

    // Field Variables
    @SerializedName("id")
    private String eventId;

    @SerializedName("title")
    private String eventTitle;

    @SerializedName("description")
    private String description;

    @SerializedName("link")
    private String eventLink;

    @SerializedName("categories")
    private ArrayList<Category> eventCategories;

    @SerializedName("sources")
    private ArrayList<Source> eventSources;

    @SerializedName("points")
    private ArrayList<Point> eventPoints;

    @SerializedName("polygons")
    private ArrayList<Polygon> eventPolygons;

    // Constructor

    // Point Constructor
    public DisasterEvent(String eventId, String eventTitle, String description, String eventLink, ArrayList<Category> eventCategories, ArrayList<Source> eventSources, ArrayList<Point> eventPoints) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.description = description;
        this.eventLink = eventLink;
        this.eventCategories = eventCategories;
        this.eventSources = eventSources;
        this.eventPoints = eventPoints;
    }

    // Polygon Constructor
    public DisasterEvent(String eventId, String eventTitle, String description, String eventLink, ArrayList<Category> eventCategories, ArrayList<Source> eventSources, ArrayList<Polygon> eventPolygons, boolean isPolygon) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventLink = eventLink;
        this.eventCategories = eventCategories;
        this.eventSources = eventSources;
        this.eventPolygons = eventPolygons;
        this.description = description;
    }


    // Getters & Setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventLink() {
        return eventLink;
    }

    public void setEventLink(String eventLink) {
        this.eventLink = eventLink;
    }

    public ArrayList<Category> getEventCategories() {
        return eventCategories;
    }

    public void setEventCategories(ArrayList<Category> eventCategories) {
        this.eventCategories = eventCategories;
    }

    public ArrayList<Source> getEventSources() {
        return eventSources;
    }

    public void setEventSources(ArrayList<Source> eventSources) {
        this.eventSources = eventSources;
    }

    public ArrayList<Point> getEventPoints() {
        return eventPoints;
    }

    public void setEventPoints(ArrayList<Point> eventPoints) {
        this.eventPoints = eventPoints;
    }

    public ArrayList<Polygon> getEventPolygons() {
        return eventPolygons;
    }

    public void setEventPolygons(ArrayList<Polygon> eventPolygons) {
        this.eventPolygons = eventPolygons;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DisasterEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventTitle='" + eventTitle + '\'' +
                ", description='" + description + '\'' +
                ", eventLink='" + eventLink + '\'' +
                ", eventCategories=" + eventCategories +
                ", eventSources=" + eventSources +
                ", eventPoints=" + eventPoints +
                ", eventPolygons=" + eventPolygons +
                '}';
    }
}
