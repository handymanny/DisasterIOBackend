package Services;

import DataController.CrowdDataController;
import Objects.MarkerMessage;

public class CrowdService {

    // Controller
    private CrowdDataController crowdDataController;

    public CrowdService() {
        crowdDataController = new CrowdDataController();
    }

    // Methods
    public String getCrowdEvent (MarkerMessage mkMsg) {
        return crowdDataController.getCrowdEvent(mkMsg);
    }
}
