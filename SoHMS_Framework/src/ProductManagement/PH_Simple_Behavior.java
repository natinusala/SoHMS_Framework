package ProductManagement;

import OrdersManagement.ROH;
import OrdersManagement.ThreadCommunicationChannel;
import ResourceManagement.ResourceHolon;
import ResourceManagement.Transporter;
import directoryFacilitator.DirectoryFacilitator;
import OrdersManagement.POH;
import mservice.MService;

import java.util.ArrayList;
import java.util.HashMap;

public class PH_Simple_Behavior implements PH_Behavior_Planner {

    public ProductHolon ph;
    public ArrayList<POH> p_ohs = new ArrayList<>();

    public PH_Simple_Behavior(ProductHolon ph) {
        this.ph = ph;
    }

    @Override
    public POH getPOH() {
        return null; //Unused
    }

    @Override
    public void setNewPlan(String actualPort, int stateID, MService ServiceType, DirectoryFacilitator df) {

    }

    @Override
    public void setDispatchPlan(String actualPort, int stateID, MService dispatchServ_Type, DirectoryFacilitator df) {

    }

    @Override
    public void run() {
        System.out.println("[PH] PH Simple Behavior running...");
        while(ph.getAssociatedResource().getPortStatus() == Transporter.TransporterState.UNKNOWN){
            Thread.yield(); // wait until the transporter has been located in the System.
        }
        System.out.println("[PH] Transporter found!");


    }
}
