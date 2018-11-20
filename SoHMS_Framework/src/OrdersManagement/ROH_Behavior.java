package OrdersManagement;

import java.util.*;
import mservice.*;
import ProductManagement.ProductHolon;
import ResourceManagement.*;
import Crosscutting.*;
import directoryFacilitator.DirectoryFacilitator;

public abstract class ROH_Behavior{

	//Attributes
	protected ResourceHolon rh;

	//Getters and Setters
	public ResourceHolon getRh() {
		return rh;
	}
	public void setRh(ResourceHolon rh) {
		this.rh = rh;
	}

	//methods
	public abstract MServiceSpecification requestServiceExe(ProductHolon client,PathState prodTask, ROH roh, DirectoryFacilitator df);

	public  abstract boolean requestPortPermition(ProductHolon client,String finalPort, long timeFromNow, ROH roh);
	
	private  long getProcessTime(PathState prodTask){
		//Get the Service Production Time
		long processTime;
		Pair<Integer,Long> method_time= new Pair<Integer, Long>(null, new Long(Long.MAX_VALUE));;
		MServiceImplentation serviceImp = rh.getRoh().associatedRH.getServByType(prodTask.service.getMServiceType());
		ArrayList<Integer> methods =serviceImp.getMatchingMethods(prodTask.service);

		ProcessMethod method;

		if(prodTask.methodID== null){ 	

			// METHOD IS UNKNOWN
			for (int i = 0; i < methods.size(); i++) { // get the fastest of the matching methods
				Long time = rh.getSil().getMethodTime(methods.get(i));
				if(time <= method_time.getSecond()){
					method_time.setFirst(methods.get(i));
					method_time.setSecond(time);
				}
			}
			method= serviceImp.getProcessMethod(method_time.getFirst());
			prodTask.methodID = method_time.getFirst(); // define a method to the task
		}
		else{ 
			// METHOD KNOWN
			processTime= rh.getSil().getMethodTime(prodTask.methodID);
			method = serviceImp.getProcessMethod(prodTask.methodID);
		}

		// GET SETUP TIME, IF ANY
		if(rh.getRoh().currentSetup!= method.getSetup()){

			// Compute Setup Time between setups
			processTime = rh.getSil().getSetupTime(rh.getRoh().currentSetup, method.getSetup()) + rh.getSil().getMethodTime(method.getId());
			return processTime;

		}else return method_time.getSecond();		 
	}

	/**
	 * returns
	 *  the task TIME if yes in milliseconds
	 *  -1 if no time to execute but can negotiate
	 *  -2 if no time to negotiate;
	 * @return
	 */
	protected long haveTimeToDoIT(PathState prodTask, ROH roh) {
		
		ResourceHolon rh= roh.associatedRH;
		int capacity=1;
		
		//BufferedRH brh = (BufferedRH)(roh.associatedRH);
		//check capacity
		
			//THE RESOUCE IS CURRENTLY UNAVAILABLE? TRY LATER
		if( roh.numOfCurrentExecutions>=capacity){
			//System.out.println("ROH Busy: "+roh.associatedRH.getName());
			return -1; // reject the action as resource is busy ( POH must deal with this)
		}
		//THERE ARE NO PLANS
		if(rh.getResourceSchedule().isEmpty()){
			//Get the Service Production Time + setup time
			long processTime = getProcessTime(prodTask);
			return processTime;  // yes there is time , there is nothing planned
		}
		//THERE ARE PLANS
		else{
			//Get the time to the next Service in plans
			Task_RH nextTask= rh.getResourceSchedule().getFirst();
			Date time=nextTask.startTime; 						
			long  lapse = time.getTime()-System.currentTimeMillis();
			
			//NO TIME TO NEGOTIATE
			if(lapse < roh.negociationTime){
				return -2;  		// no time to negotiate
			}
			//THERE IS TIME TO NEGOTIATE
			else{
				//Get the Service Production Time + setup time
				long processTime = getProcessTime(prodTask);
				
				// See if it fits before next task
				lapse = time.getTime()-System.currentTimeMillis();
				
				//IT FITS IN PLANS
				if (lapse>= processTime){
					return processTime; // there is time
				}
				// IT DOES NOT FIT IN PLANS RIGHT NOW
				else { return -1;} // There is no time to execute, try again later
			}	
		}
	}
	
	public abstract String requestDefaultTransfer(Transporter transporter, String port); //will be implemented in Buffered and Router Roh behaviors.
	
		
}
