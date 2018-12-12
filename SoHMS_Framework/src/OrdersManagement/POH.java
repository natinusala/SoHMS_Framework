package OrdersManagement;

import ProductManagement.ProductHolon;
import ProductManagement.ProductionProcess;
import ResourceManagement.ResourceHolon;
import mservice.MService;
import mservice.MServiceSpecification;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class POH{

	//Attributes
	public POH_Behavior behavior;
	public ProductHolon associatedPH;

	public String productPosition = "SOURCE"; //Having it at NULL means position of productPositionResource
	public ResourceHolon productPositionResource = null;

	public synchronized void setProductPosition(ResourceHolon productPosition)
	{
		this.productPositionResource = productPosition;
		this.productPosition = null;
	}

	public synchronized void setProductPosition(String productPosition)
	{
		this.productPositionResource = null;
		this.productPosition = productPosition;
	}

	protected ROH roh;

	public int evolve(MServiceSpecification serv) {
		return services.evolve(serv);
	}

	public void setROH(ROH roh)
	{
		this.roh = roh;
	}

	private ProductionProcess services;
	
	//Constructor
	public POH(ProductHolon ph, POH_Behavior behavior) {
		this.behavior= behavior;
		this.associatedPH= ph;

		services = ph.getRecipe().clone();
	}

	public void launch()
	{
		new Thread(behavior).start();
	}

	public synchronized MServiceSpecification getNextService()
	{
		ArrayList<MServiceSpecification> alternatives = services.getAlternatives();
		return alternatives.size() > 0 ? alternatives.get(0) : null; //Return the first service to do next
	}
	
	//Getters and Setters
	public POH_Behavior getBehavior() {
		return behavior;
	}
	public void setBehavior(POH_Behavior behavior) {
		this.behavior = behavior;
	}
	public ProductHolon getAssociatedPH() {
		return associatedPH;
	}
	public void setAssociatedPH(ProductHolon associatedPH) {
		this.associatedPH = associatedPH;
	}
}
