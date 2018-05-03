package ProductManagement;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import Crosscutting.*;
import MService.MService;
import OrdersManagement.OrderManager;


public abstract class ProductHolon{
  
	//Attributes 
	public  static int phCount= 0;
	public  static int phListSize= 1000;
	
	protected int id;
	protected OrderManager ordermanager;
	protected ProductionPlan productionPlan;
	protected ConcurrentHashMap<Integer,State> actionsPlan;
	protected ProductionProcess recipe;
	protected Behavior_PH_Planner exploreBehavior;

	//Constructor
	public ProductHolon(){
		this.id= (phCount % phListSize) +1;
		phCount=this.id;
		productionPlan= new ProductionPlan();
		actionsPlan= new ConcurrentHashMap<>(); //A terminer
	}
	public ProductHolon(OrderManager orderManager, ProductionProcess recipe){
		this.ordermanager= orderManager;
		this.recipe= recipe;
	}
	//methods
	abstract void launch();
	public void productTerminated() {
		ordermanager.phIsFinised(this);
	}
}
