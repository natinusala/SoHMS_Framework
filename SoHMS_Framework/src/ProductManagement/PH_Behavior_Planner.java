package ProductManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import OrdersManagement.POH;
import ResourceManagement.ResourceHolon;
import Workshop.LayoutMap;
import Crosscutting.*;
import MService.MService;
import MService.MServiceSpecification;


public  class PH_Behavior_Planner implements Runnable {
	
	//Attributes
	protected ProductHolon ph;
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
    	//Get PathArcs to all possible Alternatives
		ArrayList<PathArc> nextStepProductionPlans= getNextPathArcs(ph.getRecipe(), stateID, actualPort); 
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
	/** * Computes an Automaton plan to the alternatives from a given position and a production state
	    * @param actualPort
	    * @param stateID
	 */
	public void setDispatchPlan(String actualPort, int stateID, MService dispatchServ_Type) {
		// Reset plans to zero
	    this.ph.setProductionPlan(new LayoutMap());
		this.ph.setActionsPlan(new ConcurrentHashMap<Integer, State>());
		
		MServiceSpecification dispatchService = new MServiceSpecification(dispatchServ_Type);
		ArrayList<PathArc> nextStepProductionPlans= getNextPathArcs(ph.getRecipe(), stateID, actualPort); 
		
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
	public void run() {}
    public  ArrayList<PathArc> getNextPathArcs(ProductionProcess recipe,int processIndex, String startingPort){
		
		ArrayList<PathArc> nextPathArcs = new ArrayList<PathArc>();
		//Get Alternative Services
		ArrayList<MServiceSpecification> altServ= recipe.getAlternatives();

		//Generate Next Possible Path States
		ArrayList<PathState> nextPathStates= new ArrayList<PathState>();
		
			// NEXT SERVICE?
			for (int i = 0; i < altServ.size(); i++) {
				//PROVIDERS OF NEXT SERVICE?
				 HashSet<Pair<ResourceHolon,Double>> providers=null;
				//=AppSOHMS.df.getProviders(altServ.get(i));				 
				//------------------------------------------------------------------------
				//Print Providers
				 System.out.println("Number of Providers:"+ providers.size());
				 System.out.println("Of "+ altServ.get(i).toString());
				 for (Pair<ResourceHolon,Double> rh : providers) {
								System.out.println(rh.getFirst().getName());
							}
							
							//--------------------------------------------------------
				for( Pair<ResourceHolon,Double> provider: providers){
					//EACH INPUTPORT OF PROVIDER?
					for(String inPort: provider.getFirst().getInputPorts()){
						for(String outPort: provider.getFirst().getOutputPorts()){
							nextPathStates.add(new PathState(altServ.get(i), provider.getFirst(), inPort, outPort, processIndex+1, provider.getSecond()));
						}
					}
					
				}	
			}
			
		//Get  ROUTES to the Next Path State
		for (PathState nextPathState: nextPathStates){
			
		// ROUTES to next RESOURCE?
			ArrayList<TerminalSequence>routes = null;
			//= AppSOHMS.df.getRoutes_NoLoops(startingPort, nextPathState.inputPort);
			
		//Create PathArc for each route
			if( nextPathState.inputPort.equalsIgnoreCase(startingPort)) {
				nextPathArcs.add(new PathArc(null, nextPathState)); //new output arc to the list
			}
				
			for(TerminalSequence route : routes){  
				nextPathArcs.add(new PathArc(route, nextPathState)); //new output arc to the list
			}										
		}
		return nextPathArcs;
		
	}

}
