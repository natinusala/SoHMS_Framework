package OrdersManagement;

import java.util.*;
import MService.*;
import ProductManagement.ProductHolon;
import ResourceManagement.*;
import Crosscutting.*;
import DirectoryFacilitator.DirectoryFacilitator;

public  class ROH_Behavior{

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
	public MServiceSpecification requestServiceExe(ProductHolon client,PathState prodTask, ROH roh, String serviceName){
		long time;
		MServiceImplentation serviceImp= null;
		HashSet<ResourceHolon> portOwners= null;
		MServiceSpecification defaultservice = null;
		synchronized (this) {
			//IF THERE IS TIME 
			time= haveTimeToDoIT(prodTask, roh); // return the Task Time if possible, -1 if not possible, -2 with no execute default as there no time left
			//THERE IS TIME TO DO IT ("TIME" IS THE FINISH TIME OF THE TASK)
			if(time>=0) {
				serviceImp = roh.associatedRH.getServByType(prodTask.service.getMServiceType());
				//ASK FOR PERMIT OF PORT TO DESTINATION
				String finalPort= prodTask.outputPort; // All task must be created with an output port.

				if(prodTask.service.getMServiceType().getName().equalsIgnoreCase(serviceName)){
					portOwners = DirectoryFacilitator.getPortOwners(finalPort);
					for (ResourceHolon owner : portOwners) {
						if(!owner.getRoh().requestPortPermition(client, finalPort, time)){
							return null; // NO PERMIT
						}
					}
				}
				roh.numOfCurrentExecutions++; // PERMIT WAS GRANTED
				System.out.println("++  ROH_Behavior1 "+rh.getName()+" rh.roh.numOfCurrentExecutions="+rh.getRoh().getNumOfCurrentExecutions());

			}
			//NO TIME RIGHT NOW ( RH BUSY IN THIS MOMENT or in the time requested)
			else if(time==-1){
				return null; //reject the Service Request (have plans or executing something)
			}
			// NO TIME TO NEGOTIATE //TODO  //evaluate pertinence of this case
			else{
				roh.numOfCurrentExecutions++; // Reserve occupation for default actions ( will keep 
				System.out.println("++  ROH_Behavior2 "+rh.getName()+" rh.roh.numOfCurrentExecutions="+rh.getRoh().getNumOfCurrentExecutions());
			}
		}
		//EXECUTE THE SERVICE
		if(time>=0){
			//At this point either there are no owners or all owners agreed a permit of the port.

			// Put the right Setup
			ProcessMethod method = serviceImp.getProcessMethod(prodTask.methodID);
			if( roh.currentSetup!= method.getSetup()){
				rh.getSil().changeSetup(method.getSetup());
			}

			if(prodTask.service.getMServiceType().getName().equalsIgnoreCase(serviceName)){
				// Change status of Pallet to InTransition
				String startport =prodTask.service.getParameterByName("StartPort").getValue(); 
				String finalPort =prodTask.service.getParameterByName("FinalPort").getValue();
				//client.associatedPallet.declareInTransition(startport, finalPort);
			}

			//Send the execution Command
			System.out.println("++  ROH_Behavior3 "+rh.getName()+" SendServiceToField ="+prodTask.service);
			boolean succesfullExec= rh.getSil().sendServiceToField(prodTask.service, client, prodTask.methodID);  // returns when execution has been completed
			roh.numOfCurrentExecutions--;//Register execution, liberate resource
			if(prodTask.service.getMServiceType().getName().equalsIgnoreCase(serviceName)){
				// Update new position of the Pallet 
				String finalPort =prodTask.service.getParameterByName("FinalPort").getValue();
				System.out.println("Simple_ROH_Behavior "+client.getAssociatedResource());
				// client.associatedPallet.upDatePosition(finalPort); 
			}

			return prodTask.service;
		}
		else{
			// If no time to negociate
			defaultservice = rh.getSil().defaultAction(client.getAssociatedResource(),prodTask.inputPort); // execute a default dispatch action and notify this action to the P-OH
			roh.numOfCurrentExecutions--; // service finished , liberate resource
			System.out.println("++  ROH_Behavior "+rh.getName()+" rh.roh.numOfCurrentExecutions= "+rh.getRoh().numOfCurrentExecutions);
		}
		return  defaultservice;	
	}

	public  boolean requestPortPermition(ProductHolon client,String finalPort, long timeFromNow, ROH roh) {

		ResourceHolon rh = roh.getAssociatedRH();

		// Create a new iterator starting form present
		ListIterator<Task_RH> exploreIterator= rh.getResourceSchedule().listIterator(); // Raw type

		while(exploreIterator.hasNext()){
			Task_RH nextTask=exploreIterator.next();
			//check if there is a service planed during that time
			if(nextTask.finishTime.getTime()> System.currentTimeMillis()+timeFromNow){
				if(nextTask.startTime.getTime()>= System.currentTimeMillis()+timeFromNow+ roh.negociationTime){
					//Check if the service uses the Port
					if(nextTask.inPort.equalsIgnoreCase(finalPort)){
						return false; //there is one service occupying the port
					} else return true; //The service does not ocuppy the port ( supposition that RH can only make one service at the time)
				}else return true; // there is time to accept it and negotiate
			}
		}
		return true; // there are no plans so go ahead!	
	}

	/**
	 * The Manufacturing resource will use the input Port all time during the execution of a Service.
	 * Thus it will return false if there is a service during the requested time;
	 * This function is called always in reactive mode
	 * Note: Might need to add a lapse to allow dealing with the product
	 */
	protected long haveTimeToDoIT(PathState prodTask, ROH roh) {
		ResourceHolon rh= roh.getAssociatedRH();
		int capacity=1;
		//BufferedRH brh = (BufferedRH)(roh.associatedRH);
		//check capacity
		//THE RESOUCE IS CURRENTLY UNAVAILABLE? TRY LATER
		if( roh.getNumOfCurrentExecutions()>=capacity){
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
			if(lapse < roh.getNegociationTime()){
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

	public String requestDefaultTransfer(Transporter transporter, String port) {	
		return null;
	}
}
