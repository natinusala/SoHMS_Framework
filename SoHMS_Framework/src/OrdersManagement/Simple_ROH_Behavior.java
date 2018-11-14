package OrdersManagement;

import java.util.HashSet;
import java.util.ListIterator;

import Crosscutting.PathState;
import DirectoryFacilitator.DirectoryFacilitator;
import mservice.MServiceImplentation;
import mservice.MServiceSpecification;
import mservice.ProcessMethod;
import ProductManagement.ProductHolon;
import ResourceManagement.ResourceHolon;
import ResourceManagement.Task_RH;
import ResourceManagement.Transporter;


/**
 * This behavior represents the simplest that applies to most of Resources.
 *  Capacity of one unit at the time.
 *  One input port, occupation of port during service execution
 *  Different setups
 *  Welcomes reactive and predictive behavior.
 * @author gamboa-f
 *
 */
public class Simple_ROH_Behavior extends ROH_Behavior {
	
	

//PUBLIC METHODS----------------------------------------------
	@Override
	public  MServiceSpecification requestServiceExe(ProductHolon client,PathState prodTask, ROH roh,DirectoryFacilitator df) {
		long time;
		MServiceImplentation serviceImp= null;
		HashSet<ResourceHolon> portOwners= null;
		
	//Synchronized segment to Analyse and Reserve execution
		synchronized (this) {
			//IF THERE IS TIME 
			 time= haveTimeToDoIT(prodTask, roh); // return the Task Time if possible, -1 if not possible, -2 with no execute default as there no time left
			
			 //THERE IS TIME TO DO IT ("TIME" IS THE FINISH TIME OF THE TASK)
			 if(time>=0) {
				 serviceImp = roh.associatedRH.getServByType(prodTask.service.getMServiceType());
				
				//ASK FOR PERMIT OF PORT TO DESTINATION
				 String finalPort= prodTask.outputPort; // All task must be created with an output port.
				
				 if(prodTask.service.getMServiceType().getName().equalsIgnoreCase("Transport")){
					portOwners = df.getPortOwners(finalPort);
					 for (ResourceHolon owner : portOwners) {
							if(!owner.getRoh().requestPortPermition(client,finalPort,time)){
								return null; // NO PERMIT
							}
					}
				}
				
				roh.numOfCurrentExecutions++; // PERMIT WAS GRANTED
				System.out.println("++  Simple_ROH_Behavior1 "+rh.getName()+" rh.roh.numOfCurrentExecutions="+rh.getRoh().getNumOfCurrentExecutions());
				
			}
			 //NO TIME RIGHT NOW ( RH BUSY IN THIS MOMENT or in the time requested)
			else if(time==-1){
				return null; //reject the Service Request (have plans or executing something)
			}
			 // NO TIME TO NEGOTIATE //TODO  //evaluate pertinence of this case
			else{
				roh.numOfCurrentExecutions++; // Reserve occupation for default actions ( will keep 
				System.out.println("++  Simple_ROH_Behavior2 "+rh.getName()+" rh.roh.numOfCurrentExecutions="+rh.getRoh().getNumOfCurrentExecutions());
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
				
					 if(prodTask.service.getMServiceType().getName().equalsIgnoreCase("Transport")){
						// Change status of Pallet to InTransition
						String startport =prodTask.service.getParameterByName("StartPort").getValue(); 
						String finalPort =prodTask.service.getParameterByName("FinalPort").getValue();
						client.getAssociatedResource().declareInTransition(startport, finalPort); //AssociatedPallet
					 }
					 
						
				//Send the execution Command
				System.out.println("++  Simple_ROH_Behavior3 "+rh.getName()+" SendServiceToField ="+prodTask.service);
				@SuppressWarnings("unused")
				boolean succesfullExec= rh.getSil().sendServiceToField(prodTask.service, client, prodTask.methodID);  // returns when execution has been completed
				roh.numOfCurrentExecutions--;//Register execution, liberate resource
				
						if(prodTask.service.getMServiceType().getName().equalsIgnoreCase("Transport")){
							// Update new position of the Pallet 
					 		 String finalPort =prodTask.service.getParameterByName("FinalPort").getValue();
					 		 System.out.println("Simple_ROH_Behavior "+client.getAssociatedResource());
					 		 client.getAssociatedResource().updatePosition(finalPort); //associatedResource
					 	}

				return prodTask.service;
			}
			else{
				// If no time to negociate
				MServiceSpecification defaultservice = rh.getSil().defaultAction(client.getAssociatedResource(),prodTask.inputPort); // execute a default dispatch action and notify this action to the P-OH
				roh.numOfCurrentExecutions--; // service finished , liberate resource
				System.out.println("++  Simple_ROH_Behavior4 "+rh.getName()+" rh.roh.numOfCurrentExecutions="+rh.getRoh().numOfCurrentExecutions);
				return  defaultservice;			}
	
	}
//--------------------------------------------------------------------------
	/**
	 * The Manufacturing resource will use the input Port all time during the execution of a Service.
	 * Thus it will return false if there is a service during the requested time;
	 * This function is called always in reactive mode
	 * Note: Might need to add a lapse to allow dealing with the product
	 */
	@Override
	public  boolean requestPortPermition(ProductHolon client,String finalPort, long timeFromNow, ROH roh) {
		ResourceHolon rh = roh.associatedRH;
		
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
	@Override
	//We don't need it here we will remove it.
	public String requestDefaultTransfer(Transporter transporter, String port) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}


