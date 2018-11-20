package ProductManagement;

import directoryFacilitator.DirectoryFacilitator;
import OrdersManagement.POH;
import mservice.MService;

public class PH_Simple_Behavior implements PH_Behavior_Planner {
    @Override
    public POH getPOH() {
        return null;
    }

    @Override
    public void setNewPlan(String actualPort, int stateID, MService ServiceType, DirectoryFacilitator df) {

    }

    @Override
    public void setDispatchPlan(String actualPort, int stateID, MService dispatchServ_Type, DirectoryFacilitator df) {

    }

    @Override
    public void run() {

    }
}
