package ProductManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import OrdersManagement.POH;
import ResourceManagement.ResourceHolon;
import Workshop.LayoutMap;
import Crosscutting.*;
import DirectoryFacilitator.DirectoryFacilitator;
import MService.MService;
import MService.MServiceSpecification;


public  interface PH_Behavior_Planner extends Runnable{
	
	public POH getPOH();
	public void setNewPlan(String actualPort, int stateID,MService ServiceType, DirectoryFacilitator df);
	void setDispatchPlan(String actualPort, int stateID,MService dispatchServ_Type, DirectoryFacilitator df);
	
}
