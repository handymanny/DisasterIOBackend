package DataController;

import DataAccess.NasaDataAccess;
import Objects.DisasterEvent;
import Objects.DisasterEventObjects.Category;
import Objects.DisasterEventObjects.Point;
import Objects.DisasterEventObjects.Polygon;
import Objects.DisasterEventObjects.Source;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NasaDataController {

    // Field Variables
    private NasaDataAccess nasaData;
    private Logger log;

    // Constructor
    public NasaDataController () {
        // Default Constructor
        nasaData = new NasaDataAccess();
        log = LoggerFactory.getLogger(NasaDataController.class);
    }

    // Getters & Setters
    public ArrayList<DisasterEvent> getNasaEventData () {
        // Store locally after called
        JSONObject data = nasaData.getNasaEventData().getBody().getObject();
        ArrayList<DisasterEvent> disasterEvents = new ArrayList<>();
        boolean hasPolygon = false;

        try {
            // Get events list
            JSONArray events = data.getJSONArray("events");

            // Iterate Through Array
            for (int i = 0; i < events.length(); i++) {
                // Create Json object from list item
                JSONObject tempObj = events.getJSONObject(i);

                // Get event info not in lists
                String eventId = tempObj.getString("id");
                String eventTitle = tempObj.getString("title");
                String description = tempObj.getString("description");
                String eventLink = tempObj.getString("link");

                // Get categories array & Iterate through categories
                JSONArray jsonCategory = tempObj.getJSONArray("categories");
                ArrayList<Category> categories = new ArrayList<>();

                for (int j = 0; j < jsonCategory.length(); j++) {
                    // Create temp object for each category
                    JSONObject temp = jsonCategory.getJSONObject(j);

                    // Get category id and title
                    int categoryId = temp.getInt("id");
                    String categoryTitle = temp.getString("title");

                    //Create new category in the list
                    categories.add(new Category(categoryId, categoryTitle));
                }

                // Get sources array & Iterate through sources
                JSONArray jsonSources = tempObj.getJSONArray("sources");
                ArrayList<Source> sources = new ArrayList<>();

                for (int j = 0; j < jsonSources.length(); j++) {
                    // Create temp object for each category
                    JSONObject temp = jsonSources.getJSONObject(j);

                    // Get category id and title
                    String sourceId = temp.getString("id");
                    String sourceLink = temp.getString("url");

                    //Create new category in the list
                    sources.add(new Source(sourceId, sourceLink));
                }

                // Get geometric array & Iterate through points or polygons
                JSONArray jsonGeometries = tempObj.getJSONArray("geometries");
                ArrayList<Point> points = new ArrayList<>();
                ArrayList<Polygon> polygons = new ArrayList<>();

                for (int j = 0; j < jsonGeometries.length(); j++) {
                    // Create temp object for each category
                    JSONObject temp = jsonGeometries.getJSONObject(j);

                    // Get category id and title
                    String geometriesDate = temp.getString("date");
                    String geometriesType = temp.getString("type");

                    if (geometriesType.equalsIgnoreCase("Point")) {

                        // Create new Point object
                        JSONArray point = temp.getJSONArray("coordinates");

                        // Store Points
                        ArrayList<Float> vectors = new ArrayList<>();
                        vectors.add(point.getFloat(0));
                        vectors.add(point.getFloat(1));

                        // Add points
                        points.add(new Point(geometriesDate, geometriesType, vectors));
                    } else if (geometriesType.equalsIgnoreCase("Polygon")) {
                        // Set boolean
                        hasPolygon = true;

                        // Create polygons object
                        JSONArray polygon = temp.getJSONArray("coordinates");

                        // Store Polygon points
                        Map<Integer, ArrayList<Float>> vectors = new HashMap<>();

                        // Loop through top layer array
                        for (int k = 0; k < polygon.length(); k++) {
                            // Create object with values
                            JSONArray tempOuter = polygon.getJSONArray(k);

                            for (int p = 0; p < tempOuter.length(); p ++) {
                                // Add Value
                                ArrayList<Float> plotPoints = new ArrayList<>();
                                plotPoints.add(tempOuter.getJSONArray(p).getFloat(0));
                                plotPoints.add(tempOuter.getJSONArray(p).getFloat(1));

                                // Add values to vectors
                                vectors.put(k, plotPoints);

                                // Add to Polygons
                                polygons.add(new Polygon(geometriesDate, geometriesType, vectors));
                            }
                        }
                    }

                }

                //Create Disaster event object
                if (hasPolygon) {

                    // Create object
                    DisasterEvent disasterEvent = new DisasterEvent(eventId, eventTitle, description, eventLink, categories, sources, polygons, hasPolygon);
                    disasterEvents.add(disasterEvent);

                    // Swap
                    hasPolygon = false;

                } else {

                    // Create Object
                    DisasterEvent disasterEvent = new DisasterEvent(eventId, eventTitle, description, eventLink, categories, sources, points);
                    disasterEvents.add(disasterEvent);
                }
            }

            // Return populated object
            log.info("Disaster Events Parsed!");
            return disasterEvents;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
