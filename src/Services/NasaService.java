package Services;

import DataController.NasaDataController;
import Objects.DisasterEvent;

import java.util.ArrayList;

public class NasaService {

    // Field Variables
    private NasaDataController nasaDataController;

    // Constructor
    public NasaService() {
        nasaDataController = new NasaDataController();
    }

    // Methods
    public ArrayList<DisasterEvent> getNasaEventData() {
        return nasaDataController.getNasaEventData();
    }
}
