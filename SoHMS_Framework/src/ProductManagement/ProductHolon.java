package ProductManagement;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import Crosscutting.*;
import MService.MService;
import OrdersManagement.OrderManager;
import ResourceManagement.Resource;
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
}
