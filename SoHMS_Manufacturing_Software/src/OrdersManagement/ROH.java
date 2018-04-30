package OrdersManagement;

import Crosscutting.PathState;
import MService.MServiceImplentation;
import ProductManagement.ProductHolon;
import ResourceManagement.ResourceHolon;




public class ROH {
	
	//Attributes
	private ResourceHolon associatedRH;
	private long negociationTime;
	private int currentSetup;
	private ROH_Behavior  behavior;	
	private Integer numOfCurrentExecutions;

	//Constructors
	public ROH(ResourceHolon rh, ROH_Behavior behavior ) {
		this.associatedRH= rh;
		this.behavior= behavior;
		behavior.setRh(rh);					// Associate the behavior to the POH
		this.negociationTime= (5*100); 		// 0.5 seconds
		this.numOfCurrentExecutions=0;
		this.currentSetup= 1;				// initialize all RH with setup 1
	}
	
	//Setters and Getters
	public ResourceHolon getAssociatedRH() {
		return associatedRH;
	}
	public void setAssociatedRH(ResourceHolon associatedRH) {
		this.associatedRH = associatedRH;
	}
	public long getNegociationTime() {
		return negociationTime;
	}
	public void setNegociationTime(long negociationTime) {
		this.negociationTime = negociationTime;
	}
	public int getCurrentSetup() {
		return currentSetup;
	}
	public void setCurrentSetup(int currentSetup) {
		this.currentSetup = currentSetup;
	}
	public ROH_Behavior getBehavior() {
		return behavior;
	}
	public void setBehavior(ROH_Behavior behavior) {
		this.behavior = behavior;
	}
	public Integer getNumOfCurrentExecutions() {
		return numOfCurrentExecutions;
	}
	public void setNumOfCurrentExecutions(Integer numOfCurrentExecutions) {
		this.numOfCurrentExecutions = numOfCurrentExecutions;
	}

	//Methods
	/**
	 * Calling this function specifically says that the service was not planed and that it is being requested at this moment.
	 * Returns the service that was executed or null if rejected
	 * All execution functions 
	 */
	public  MServiceImplentation requestServiceExe(ProductHolon client,PathState prodTask) {
		return behavior.requestServiceExe(client, prodTask, this);
	}
	/**
	 * Will indicate if the inport port of the resource will be available at the time requested
	 * @param finalPort
	 * @param timeFromNow
	 * @param rohimport sohmsPlateform.resourceHolon.Transporter;

	 * @return
	 */
	public boolean requestPortPermit(ProductHolon client, String finalPort, long timeFromNow) {
		return behavior.requestPortPermit(client,finalPort, timeFromNow, this);
	}
	/*
	public String requestDefaultTransfer(Transporter transporter, String port) {
		return behavior.requestDefaultTransfer(transporter, port);
	}
	*/
}
