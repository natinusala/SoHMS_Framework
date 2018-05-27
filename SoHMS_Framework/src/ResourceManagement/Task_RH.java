package ResourceManagement;

import java.util.Date;

import MService.MServiceSpecification;
import ProductManagement.ProductHolon;

public class Task_RH {

	
	//ATTRIBUTS-------------------------------
	public MServiceSpecification service;
	public ProductHolon client;
	public String setup;
	public long processingTime;
	public int methodID;
	
	public Date startTime;
	public Date finishTime;
	public int id;
	
	public String inPort;
	public String outPort;
	
	private static int taskCount= 0;
	private static int taskListSize= 1000000;
	//CONSTRUCTORS-------------------------------
	public Task_RH(){
		 startTime = null;
		 finishTime = null;
		 this.id= (taskCount % taskListSize) +1;
		 taskCount=this.id;
	}
	//---------------------------------------------
	public Task_RH(MServiceSpecification service, ProductHolon client, String setup, int methodID, long processingTime, Date startTime, Date finishTime){
		this();
		this.service= service;
		this.client= client;
		this.processingTime= processingTime;
		this.setup= setup;
		this.startTime= startTime;
		this.finishTime= finishTime;
		this.methodID= methodID;
	}
	//PUBLIC METHODS-------------------------------
	@Override
	public String toString() {
		return "Task_RH [id=" + id + ", service=" + service + ", client="
				+ client + ", setup=" + setup + ", processingTime="
				+ processingTime + ", startTime=" + startTime + ", finishTime="
				+ finishTime + "]";
	}
	//------------------------------------------------------------
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		result = prime * result + ((setup == null) ? 0 : setup.hashCode());
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
		Task_RH other = (Task_RH) obj;
		if (client == null) {
			if (other.client != null)
				return false;
		} else if (!client.equals(other.client))
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		if (setup == null) {
			if (other.setup != null)
				return false;
		} else if (!setup.equals(other.setup))
			return false;
		return true;
	}
}
