package ProductManagement;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import MService.MService;

public class ProductHolon {
  
	//Attributes 
	private static int phCount= 0;
	private static int phListSize= 1000;
	
	private int id;
	private ProductionOrderManager ordermanager;
	private ProductionPlan productionPlan;
	private  Object actionsPlan;

	
	//constructors
	public ProductHolon() {
		this.id= (phCount % phListSize) +1;
		phCount=this.id;
		productionPlan= new ProductionPlan();
		actionsPlan= new Object();
	}
	
	
	//Getters and Setters
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

	public ProductionOrderManager getOrdermanager() {
		return ordermanager;
	}

	public void setProductionOrdermanager(ProductionOrderManager ordermanager) {
		this.ordermanager = ordermanager;
	}

	public ProductionPlan getProductionPlan() {
		return productionPlan;
	}

	public void setProductionPlan(ProductionPlan productionPlan) {
		this.productionPlan = productionPlan;
	}

	public Object getActionsPlan() {
		return actionsPlan;
	}

	public void setActionsPlan(Object actionsPlan) {
		this.actionsPlan = actionsPlan;
	}

	
	//methods

	
	
}
