package Services;

import DataController.CrowdDataController;

public class CrowdService {

    // Controller
    private CrowdDataController crowdDataController;

    public CrowdService() {
        crowdDataController = new CrowdDataController();
    }

    // Methods
    public String getCrowdEvent () {
        return crowdDataController.getCrowdEvent();
    }
}
