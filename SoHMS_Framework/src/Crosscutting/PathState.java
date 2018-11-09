package Crosscutting;

import java.util.Date;

import mservice.MServiceSpecification;
import ResourceManagement.ResourceHolon;

public class PathState {

	//Attributes----------------------------------

	private int pathStateCount= 0;
	private int pathStateListSize= 1000000;

	public MServiceSpecification service;
	public int processIndex;
	public ResourceHolon provider;
	public String inputPort;
	public String outputPort;
	public String nextStationPort; // usefull for transport, indicate the next station
	boolean isTerminal;
	public Integer methodID;

	public Date startTime;
	public Date finishTime;
	public Double cost;

	public int id;



	//Constructors----------------------------
	public PathState(){
		isTerminal = false;
		startTime = null;
		finishTime = null;
		this.id= (pathStateCount % pathStateListSize) +1;
		pathStateCount=this.id;
	}
	//-----------------------------------------------------------
	public PathState(MServiceSpecification service, ResourceHolon provider, String inputPort,String outputPort, int processIndex, Double cost){
		this();
		//just make reference to the objects. No need to create new ones
		this.service= service;
		this.provider= provider;
		this.inputPort= inputPort;
		this.outputPort= outputPort;
		this.processIndex= processIndex;
		this.cost= cost;
	}
	//PUBLIC METHODS-------------------------------------------------------------
	public Double getCost(){
		return this.cost;
	}
	//-----------------------------------------------
	@Override
	public String toString() {
		return "PathState [serviceName=" + service + ", provider="
				+ provider + ", Input port=" + inputPort + "Output port=" + outputPort + ", isTerminal="
				+ isTerminal + "]";

	}
	//---------------------------------------------------------------------------
	public String toStringDisplay (){
		String str= "("+service.getMServiceType().getName() + "/"+provider.getResourceId()+"/"+inputPort+"/"+outputPort+")";
		return str;

	}
	//--------------------------------------------------------------

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((inputPort == null) ? 0 : inputPort.hashCode());
		result = prime * result
				+ ((outputPort == null) ? 0 : outputPort.hashCode());
		result = prime * result
				+ ((provider == null) ? 0 : provider.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		return result;
	}
	//-------------------------------------------------------------
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PathState other = (PathState) obj;
		if (inputPort == null) {
			if (other.inputPort != null)
				return false;
		} else if (!inputPort.equals(other.inputPort))
			return false;
		if (outputPort == null) {
			if (other.outputPort != null)
				return false;
		} else if (!outputPort.equals(other.outputPort))
			return false;
		if (provider == null) {
			if (other.provider != null)
				return false;
		} else if (!provider.equals(other.provider))
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		return true;
	}				

}