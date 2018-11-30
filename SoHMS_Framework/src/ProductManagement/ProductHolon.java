package ProductManagement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import Crosscutting.*;
import OrdersManagement.ComInterface;
import directoryFacilitator.DirectoryFacilitator;
import Workshop.LayoutMap;
import Workshop.SimpleLayoutMap;
import mservice.MService;
import mservice.MServiceSpecification;
import OrdersManagement.OrderManager;
import ResourceManagement.ResourceHolon;
import ResourceManagement.Transporter;


public  class ProductHolon{

	//Attributes 
	public  static int phCount= 0;
	public  static int phListSize= 1000;

	private int id;
	private OrderManager ordermanager;
	private ProductionProcess recipe;
	private Transporter associatedResource; //Palette for example for transport
	private PH_Behavior_Planner exploreBehavior;
	private LayoutMap productionPlan;
	private ConcurrentHashMap<Integer,PathState> actionsPlan;
	private MService actualMservice;

	private Transporter transporter;

	//Constructor
	public ProductHolon(ComInterface comInterface){
		transporter = new Transporter(comInterface, Transporter.TransporterState.IDLE, this, 0);
		this.id= (phCount % phListSize) +1;
		phCount=this.id;
		productionPlan= new SimpleLayoutMap();
		actionsPlan= new ConcurrentHashMap<>(); //A terminer
	}
	public ProductHolon(OrderManager orderManager, ProductionProcess recipe){
		transporter = new Transporter(orderManager.comInterface, Transporter.TransporterState.IDLE, this, 0);
		this.ordermanager = orderManager;
		this.recipe = recipe;
	}

	//methods
	public void launch(DirectoryFacilitator df) {
		//1-Associate this resource to the PH after selecting a free resource
		associateResourceToPH(df); //this resouce is a Pallet
		//2-launch the behavior
		Thread phExploreThread = new Thread(exploreBehavior);
		phExploreThread.start();
	}
	
	/*public void associateResourceToPH(directoryFacilitator df) {
		//
		// * Selectioner la premire resource que arrive au port initial et qui est libre
		// * Returns an associated Ressource to the PH.
		//
		//1-Select a free resource.
		Transporter selectedPallet= null;
		// Do until succesful association
		do{
			// Update List of Free Transporters
			ArrayList<Transporter> listOFPallets= df.getFreeTransporter();
			for (Transporter t : listOFPallets) {
				if(t.portStatus== "Blocked" &&  // La Pallet est stable dans une position
						t.getAssociatedPH()==null){ // La Palette n'a pas de PH associe
					selectedPallet= t;
					break;
				}	
			}
		}while(selectedPallet==null);
		//2-Associate this resource to the PH
		this.associatedResource= selectedPallet;
	}*/

	public void associateResourceToPH(DirectoryFacilitator df)
	{
		//For now we only have one transporter
		this.associatedResource = transporter;
		System.out.println("[PH] Transporter associated to PH " + this.getId());
	}
	
	public void liberateResource() {
		//1-liberate Resource from PH
		this.associatedResource.liberate();
		//2- liberate PH from Resource
		this.associatedResource=null;
		System.out.println("[PH] Resources freed from PH " + getId());
	}
	
	public void addPathArcToExecutablePlans (ArrayList<PathArc> nextStepPlans, MService transportSer, DirectoryFacilitator df){
		//For all Alternatives
		for (int j=0; j<nextStepPlans.size(); j++) {
			PathArc plan = nextStepPlans.get(j);
			
			// TRANSPORT steps
			if(plan.route!= null){ // If need of transport services
				
				for (int i=0; i<plan.route.sequence.size();i++) {
					String actualPort;
					String nextPort;
					if( productionPlan.getEdges().isEmpty()|| i==0){ // it is the first task of the first plan
						 actualPort= associatedResource.actualPort;
						 nextPort= plan.route.sequence.get(i).label;
					
					}
					else{
						 actualPort =plan.route.sequence.get(i-1).label;
						 nextPort= plan.route.sequence.get(i).label;
					}
					//Create Transport Services Instance
					MServiceSpecification tranServSpecification= new MServiceSpecification(transportSer);
					//Set Values of Transport Service
					tranServSpecification.getParameters().get(0).setValue(actualPort); // Take actual position as Starting Point
					tranServSpecification.getParameters().get(1).setValue(nextPort); // Set the first Port to visit
					//Get Transport Provider
					//4 lines of code for get the transport rh provider.
					HashSet<Pair<ResourceHolon,Double>> transProviders= df.getProviders(tranServSpecification);
					Iterator<Pair<ResourceHolon, Double>> it = transProviders.iterator();
					Pair<ResourceHolon,Double> transProvider = it.next();
					ResourceHolon provider = transProvider.getFirst();
					//Create PathState
					PathState planState = new PathState(tranServSpecification,provider,
							tranServSpecification.getParameters().get(0).getValue(), // InPort of service
							tranServSpecification.getParameters().get(1).getValue(), // Outport of Service
							0, // We don't need process index for the Transport Services Plans
							plan.route.sequence.get(0).weight); // cost of the Transport Service
					
					if(!actionsPlan.containsValue(planState)){ // it does not contain the PathState yet
						//Add to Plan Graph and to Plan Action List
						//Add edge to Production Plan
						productionPlan.addEdge(actualPort, //Origin
								nextPort,//Destination
								Integer.toString(planState.id), //PlanAction id as label
								plan.getCost());//cost
						//Add Corresponding PathState to PlanActions
						actionsPlan.put(planState.id, planState);
					}
				}
			}
			 
			// MANUFACTURING SERVICES
			PathState manufState= nextStepPlans.get(j).nextPathState;
			if(!actionsPlan.contains(manufState)){ // it does not contain the PathState yet
				//Add edge to Production Plan
				productionPlan.addEdge(manufState.inputPort, //Origin
						manufState.outputPort,//Destination
						Integer.toString(manufState.id), //PlanAction id as label
						plan.getCost());//cost // TODO : redundant
				//Add Corresponding PathState to PlanActions
				actionsPlan.put(manufState.id, manufState);
			}

			/*
			 * So we have an Automaton with transitions labeled with the
			 * corresponding PathState and the  transition weight represents the heuristic to the objective path.
			 */
		}
		
	}

	public void productTerminated() {
		ordermanager.phIsFinised(this);
		this.associatedResource=null;
	}
	//Setters and Getters
	public static int getPhCount() {
		return phCount;
	}
	public static void setPhCount(int phCount) {
		ProductHolon.phCount = phCount;
	}
	public static int getPhListSize() {
		return phListSize;
	}
	public static void setPhListSize(int phListSize) {
		ProductHolon.phListSize = phListSize;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public OrderManager getOrdermanager() {
		return ordermanager;
	}
	public void setOrdermanager(OrderManager ordermanager) {
		this.ordermanager = ordermanager;
	}
	public LayoutMap getProductionPlan() {
		return productionPlan;
	}
	public void setProductionPlan(LayoutMap productionPlan) {
		this.productionPlan = productionPlan;
	}
	public ConcurrentHashMap<Integer, PathState> getActionsPlan() {
		return actionsPlan;
	}
	public void setActionsPlan(ConcurrentHashMap<Integer, PathState> actionsPlan) {
		this.actionsPlan = actionsPlan;
	}
	public ProductionProcess getRecipe() {
		return recipe;
	}
	public void setRecipe(ProductionProcess recipe) {
		this.recipe = recipe;
	}
	public PH_Behavior_Planner getExploreBehavior() {
		return exploreBehavior;
	}
	public void setExploreBehavior(PH_Behavior_Planner exploreBehavior) {
		this.exploreBehavior = exploreBehavior;
	}
	public Transporter getAssociatedResource() {
		return associatedResource;
	}
	public void setAssociatedResource(Transporter associatedResource) {
		this.associatedResource = associatedResource;
	}
}
