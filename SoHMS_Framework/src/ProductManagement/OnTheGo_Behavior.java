package ProductManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import Crosscutting.*;
import DirectoryFacilitator.DirectoryFacilitator;
import Workshop.GraphLayoutMap;
import Workshop.LayoutMap;
import Workshop.SimpleLayoutMap;
import mservice.*;
import OrdersManagement.*;
import ResourceManagement.*;


/**
 * This behavior  explores all the alternatives for the next step and calculate the routes to those alternatives.
 * Then it sorts the alternatives according to their static data weighted as a cost.
 * This behavior launches an Order Holon with a reactive behavior which will attempt to execute the plans as they were classified.
 * @author Francisco
 *
 */
public class OnTheGo_Behavior implements PH_Behavior_Planner, Runnable {

	//ATTRIBUTS---------------------------------------------
	public ProductHolon ph;
	public POH p_oh;
	private ArrayList<ResourceHolon> resourceCloud;

	//CONSTRUCTORS---------------------------------------------
	public OnTheGo_Behavior(ProductHolon ph, ArrayList<ResourceHolon> resourceCloud) {
		this.ph= ph;
		this.resourceCloud = resourceCloud;
	}

	//PUBLIC METHODS---------------------------------------
	@Override
	public void run() {

		System.out.println("PH Reactive Behavior Launched");

		//Wait until Locate Pallet in the System
		while(ph.getAssociatedResource().getPortStatus() == Transporter.TransporterState.UNKNOWN){
			Thread.yield(); // wait until the Pallet has been located in the System.
		}

		//		// Create Production Plan for the actual position and Production State
		//		setNewPlan(ph.associatedPallet.actualPort, ph.recipe.getStateID());

		// Create and Launch Product-Order Holon with its behavior
		POH_Behavior behavior=new Reactive_POH_Behavior();
		this.p_oh = new POH (ph, behavior); // creates and launches the P_OH behavior
		behavior.setAssociatedPOH(p_oh);
		behavior.setPlanState(ph.getAssociatedResource().getActualPort());

		// Launch POH behavior
		Thread behaviorPOH = new Thread(behavior);
		behaviorPOH.start(); // will constantly check explored production plans and will tray to execute the best classed

		/*
		 * The PO is launched and this thread  finishes. The OH thread will then continue
		 * Now the initial step has been Planned  and the OH has been launched to Execute plans
		 * The OH will then read whenever there is production plans available and will execute them. 
		 * In this first scenario is the same thread that plans and then executes (Sequential execution)
		 */
	}
	//-------------------------------------------------------
	/**
	 * Computes an Automaton plan to the alternatives from a given position and a production state
	 * @param actualPort
	 * @param stateID
	 */
	@Override
	public void setNewPlan(String actualPort, int stateID,MService ServiceType, DirectoryFacilitator df) {
		// Reset plans to zero
		LayoutMap map = new SimpleLayoutMap();
	    this.ph.setProductionPlan(map);
		this.ph.setActionsPlan(new ConcurrentHashMap<Integer, PathState>());
    	//Get PathArcs to all possible Alternatives
		ArrayList<PathArc> nextStepProductionPlans= getNextPathArcs(ph.getRecipe(), stateID, actualPort); 
		//Classify Results 
		Collections.sort(nextStepProductionPlans, new PathArcComparator());
			//Print Sorted List	
			for (PathArc pathArc : nextStepProductionPlans) {
						System.out.println(pathArc.toString());
		}
			//Create production plan of Tasks
		
				ph.addPathArcToExecutablePlans(nextStepProductionPlans, ServiceType, df);
				System.out.println("Automaton with Executable Plans Created");
			
		
   }
	//----------------------------------------------------------------------
	/**
	 * Computes an Automaton plan to the alternatives from a given position and a production state
	 * @param actualPort
	 * @param stateID
	 */
	@Override
	public void setDispatchPlan(String actualPort, int stateID, MService dispatchServ_Type, DirectoryFacilitator df) {
		// Reset plans to zero
		this.ph.setProductionPlan(new SimpleLayoutMap());
		this.ph.setActionsPlan(new ConcurrentHashMap<Integer, PathState>());

		MServiceSpecification dispatchService = new MServiceSpecification(dispatchServ_Type);
		ArrayList<PathArc> nextStepProductionPlans= getNextPathArcs(ph.getRecipe(), stateID, actualPort); 

		//Classify Results 
		Collections.sort(nextStepProductionPlans, new PathArcComparator());
		//Print Sorted List	
		for (PathArc pathArc : nextStepProductionPlans) {
			System.out.println(pathArc.toString());
		}
		//Create production plan of Tasks
		ph.addPathArcToExecutablePlans(nextStepProductionPlans,dispatchServ_Type,df);
		System.out.println("Automaton with Executable Plans Created");

	}

	public  ArrayList<PathArc> getNextPathArcs(ProductionProcess recipe,int processIndex, String startingPort){

		ArrayList<PathArc> nextPathArcs = new ArrayList<PathArc>();
		//Get Alternative Services
		ArrayList<MServiceSpecification> altServ= recipe.getAlternatives();

		//Generate Next Possible Path States
		ArrayList<PathState> nextPathStates= new ArrayList<PathState>();

		// NEXT SERVICE?
		for (int i = 0; i < altServ.size(); i++) {
			MServiceSpecification serviceSpecification = altServ.get(i);
			//PROVIDERS OF NEXT SERVICE?
			HashSet<Pair<ResourceHolon,Double>> providers=new HashSet<>();

			for (ResourceHolon resourceHolon : resourceCloud)
			{
				for (MServiceImplentation s : resourceHolon.getOfferedServices())
				{
					if (s.matchService(serviceSpecification))
						providers.add(new Pair<>(resourceHolon, 1.0)); //Everything has a cost of 1 for now
				}
			}

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
	//Getters and Setters ----------------------------------
	@Override
	public POH getPOH() {
		// TODO Auto-generated method stub
		return null;
	}


}
