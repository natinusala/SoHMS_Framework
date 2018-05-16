package ProductManagement;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import Crosscutting.*;
import DirectoryFacilitator.DirectoryFacilitator;
import MService.MService;
import OrdersManagement.OrderManager;
import ResourceManagement.Resource;
import ResourceManagement.ResourceHolon;
import Workshop.LayoutMap;





public  class ProductHolon{
  
	//Attributes 
	public  static int phCount= 0;
	public  static int phListSize= 1000;
	
	protected int id;
	protected OrderManager ordermanager;
	protected LayoutMap productionPlan;
	protected ConcurrentHashMap<Integer,State> actionsPlan;
	protected ProductionProcess recipe;
	protected PH_Behavior_Planner exploreBehavior;
	protected Resource associatedResource;

	//Constructor
	public ProductHolon(){
		this.id= (phCount % phListSize) +1;
		phCount=this.id;
		productionPlan= new LayoutMap();
		actionsPlan= new ConcurrentHashMap<>(); //A terminer
	}
	public ProductHolon(OrderManager orderManager, ProductionProcess recipe){
		this.ordermanager= orderManager;
		this.recipe= recipe;
	}
	
	//methods
	public void launch() {
		/*
		 *   1- associer une resource a un PH
		 */
		Thread phExploreThread = new Thread(exploreBehavior);
		phExploreThread.start();
		
	}
	public void associateResourceToPH(ProductHolon ph, DirectoryFacilitator df) {
		/**
		 * Selectioner la premire resource que arrive au port initial et qui est libre
		 * Returns an associated Ressource to the PH.
		 */
		 Resource selectedResource= null;
			// Do until succesful association
			do{
				// Update List of Free Transporters
				ArrayList<ResourceHolon> listOFResources= null;
				for (ResourceHolon r : listOFResources) {
					//if(r.getp== PortPositionStatus.Blocked &&  // La Pallet est stable dans une position
					  if(r.getAssociated_PH()==null){ // La Palette n'a pas de PH associé
					   selectedResource= r;
						break;
					 }	
			//	}
			}
	     }while(selectedResource==null);
	}
	public void associateResource(Resource s) {
     //Associate PH to resource
	  s.setAssociated_PH(this);
	//Associate resource to PH
	  this.associatedResource = s;
	};
	public void liberateResource() {
	  //1-liberate Resource from PH
	  this.associatedResource.liberateResource();
	  //2- liberate PH from Resource
	  this.associatedResource=null;
	};
	public void addPathArcToExecutablePlans(ArrayList<PathArc> nextStepPlans) {}
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
	public ConcurrentHashMap<Integer, State> getActionsPlan() {
		return actionsPlan;
	}
	public void setActionsPlan(ConcurrentHashMap<Integer, State> actionsPlan) {
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
	public Resource getAssociatedResource() {
		return associatedResource;
	}
	public void setAssociatedResource(Resource associatedResource) {
		this.associatedResource = associatedResource;
	}
}
