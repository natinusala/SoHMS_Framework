package ProductManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import OrdersManagement.POH;
import Protocols.Protocol_GetNextPathArcs;
import Workshop.LayoutMap;
import Crosscutting.*;
import MService.MService;
import MService.MServiceSpecification;


public class PH_Behavior_Planner implements Runnable {
	
	//Attributes
	public ProductHolon ph;
	protected POH p_oh;
	
	//Constructor
	public PH_Behavior_Planner(ProductHolon ph) {
		this.ph = ph;
	}
	
	//Methods
	public void setNewPlan(String actualPort, int stateID) {
		// Reset plans to zero
	    this.ph.setProductionPlan(new LayoutMap());
		this.ph.setActionsPlan(new ConcurrentHashMap<Integer, State>());
		ArrayList<PathArc> nextStepProductionPlans= Protocol_GetNextPathArcs.run(ph.getRecipe(), stateID, actualPort); 
		//Classify Results 
		Collections.sort(nextStepProductionPlans, new PathArcComparator());
			//Print Sorted List	
			for (PathArc pathArc : nextStepProductionPlans) {
						System.out.println(pathArc.toString());
		}
			//Create production plan of Tasks
		
				ph.addPathArcToExecutablePlans(nextStepProductionPlans);
				System.out.println("Automaton with Executable Plans Created");
			
		
   }
	/**
	    * Computes an Automaton plan to the alternatives from a given position and a production state
	    * @param actualPort
	    * @param stateID
	 */
	public void setDispatchPlan(String actualPort, int stateID, MService dispatchServ_Type) {
		// Reset plans to zero
	    this.ph.setProductionPlan(new LayoutMap());
		this.ph.setActionsPlan(new ConcurrentHashMap<Integer, State>());
		
		MServiceSpecification dispatchService = new MServiceSpecification(dispatchServ_Type);
		ArrayList<PathArc> nextStepProductionPlans= Protocol_GetNextPathArcs.run(ph.getRecipe(), stateID, actualPort); 
		
		//Classify Results 
		Collections.sort(nextStepProductionPlans, new PathArcComparator());
			//Print Sorted List	
			for (PathArc pathArc : nextStepProductionPlans) {
						System.out.println(pathArc.toString());
		}
			//Create production plan of Tasks
		
				ph.addPathArcToExecutablePlans(nextStepProductionPlans);
				System.out.println("Automaton with Executable Plans Created");
		
	}
	protected ProductHolon getPOH() {
		return this.ph;
	};
	
	public void run() {
		
	}
}
