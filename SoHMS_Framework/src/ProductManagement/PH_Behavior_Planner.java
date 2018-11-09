package ProductManagement;

import OrdersManagement.POH;
import DirectoryFacilitator.DirectoryFacilitator;
import mservice.MService;


public  interface PH_Behavior_Planner extends Runnable{
	
	public POH getPOH();
	public void setNewPlan(String actualPort, int stateID,MService ServiceType, DirectoryFacilitator df);
	void setDispatchPlan(String actualPort, int stateID,MService dispatchServ_Type, DirectoryFacilitator df);
	
}
