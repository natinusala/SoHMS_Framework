package OrdersManagement;

import Crosscutting.PathState;
import directoryFacilitator.DirectoryFacilitator;
import mservice.MServiceSpecification;
import ProductManagement.ProductHolon;
import ResourceManagement.ResourceHolon;
import ResourceManagement.Transporter;

public class ROH {

	//Attributes
	protected ResourceHolon associatedRH;
	protected long negociationTime;
	protected int currentSetup;
	protected ROH_Behavior behavior;
	protected Integer numOfCurrentExecutions;

	protected POH poh;

	public void setPOH(POH poh)
	{
		this.poh = poh;
	}

	//Constructors
	public ROH() {}

	public ROH(ResourceHolon rh, ROH_Behavior behavior ) {
		this.associatedRH= rh;
		this.behavior= behavior;
		behavior.setRh(rh);					// Associate the behavior to the POH
		this.negociationTime= (5*100); 		// 0.5 seconds
		this.numOfCurrentExecutions=0;
		this.currentSetup= 1;				// initialize all RH with setup 1
	}

	public void launch()
	{
		new Thread(behavior).start();
	}

	//Methods
	public  MServiceSpecification requestServiceExe(ProductHolon client,PathState prodTask, DirectoryFacilitator df) {
		return behavior.requestServiceExe(client, prodTask, this,df);
	}

	public boolean requestPortPermition(ProductHolon client, String finalPort, long timeFromNow) {
		return behavior.requestPortPermition(client,finalPort, timeFromNow, this);
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

	public String requestDefaultTransfer(Transporter transporter, String port) {
		return behavior.requestDefaultTransfer(transporter, port);
	}
}
