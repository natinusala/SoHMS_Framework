package Resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import Application.Application;
import ResourceManagement.*;
import ProductManagement.*;
import OrdersManagement.*;
import Crosscutting.*;
import mservice.MServiceSpecification;



public class RouterCoordinator extends Thread{

	//ATTRIBUTS-------------------------------
	public ResourceHolon rh;
	public List<Task_RH> requestInbox ;
	public boolean waiting=false;
	//CONSTRUCTORS-------------------------------
		
	public RouterCoordinator(ResourceHolon rh){
			this.rh = rh;
			this.requestInbox = Collections.synchronizedList(new ArrayList<Task_RH>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated
		}
	//PUBLIC METHODS-------------------------------
		@Override
	public void run(){
			
			while(Application.isActive){
				//WAIT IF NO MESSAGES IN THE LIST
				 // This is to avoid excessive consumption of resources
				synchronized (requestInbox) {
					try {
						if (requestInbox.isEmpty()) {
							 //System.out.println("-- RouterCoordinator "+rh.getName()+" waiting requestinbox");
							waiting=true;
							requestInbox.wait();//Wait for notification of new message
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				waiting=false;
			//NOTIFICATION RECIEVED THAT THERE ARE NEW REQUESTS
			//Read and handle all requests
			while (!requestInbox.isEmpty()){ // While there are requests in the Inbox  //TODO this can be optimized
				//System.out.println("-- RouterCoordinator "+rh.getName()+" Requestinbox trouvee");
				//System.out.println("-- RouterCoordinator "+rh.getName()+"  Requestbox size="+requestInbox.size());
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Pair<Task_RH,Boolean> request;
				synchronized (requestInbox) {
					 request = selectRequest();
				}
				// Execute or Reject Request
				synchronized (request.getFirst().service) { //Synchronized over the service instance requested. 
					if(request.getSecond() == true){
						//Send the execution Command
						//rh.roh.numOfCurrentExecutions++;
						//System.out.println("++  RouterCoordinator "+rh.getName()+" rh.roh.numOfCurrentExecutions="+rh.roh.numOfCurrentExecutions);
						
						ProductHolon client = request.getFirst().client;
						MServiceSpecification servInst= request.getFirst().service;
						int methodID = request.getFirst().methodID;
						
							// Change status of Pallet to "InTransition"
							String startport =servInst.getParameterByName("StartPort").getValue(); 
							String finalPort =servInst.getParameterByName("FinalPort").getValue();
							client.getAssociatedResource().declareInTransition(startport, finalPort);

							
							if (rh.getRoh().getAssociatedRH() instanceof BufferedRH){
								//System.out.println("== ROH_Behavior Transfert par defaut demande palette "+transporter._RFID+" e ressource "+rh.getName()+", considere BufferedRH");
							BufferedRH rh2 = (BufferedRH)(rh.getRoh().getAssociatedRH());
							//check capacity
								while(!(rh.getRoh().getNumOfCurrentExecutions()<rh2.getCapacity())){
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								}
							}else{
								//System.out.println("== ROH_Behavior Transfert par defaut demande palette "+transporter._RFID+" e ressource "+rh.getName()+", considere RH normal");
								while(!(rh.getRoh().getNumOfCurrentExecutions()<=0)){
								try {
									//System.out.println("== ROH_Behavior Transfert par defaut demande palette "+transporter._RFID+" e ressource "+rh.getName()+", numOfCurrentExecutions+"+rh.roh.numOfCurrentExecutions);
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								}
								rh.getRoh().setNumOfCurrentExecutions(0);

							}
							
							
							System.out.println("++  RouterCoodinator "+rh.getName()+" SendServiceToField ="+servInst);
							
						@SuppressWarnings("unused")
						boolean succesfullExec= rh.getSil().sendServiceToField(servInst, client, methodID);  // returns when execution has been completed
						//rh.roh.numOfCurrentExecutions--;//Register execution, liberate resource	
						//System.out.println("++  RouterCoordinator "+rh.getName()+" rh.roh.numOfCurrentExecutions="+rh.roh.numOfCurrentExecutions);
						
						((ReactiveRouter_ROH_Behavior)(rh.getRoh().getBehavior())).requestResult= request.getSecond();
																			
							// Update new position of the Pallet ( confirmed by the SIL)
						 //System.out.println("-- RouterCoordinator "+rh.getName()+"  "+client.associatedPallet);
						 client.getAssociatedResource().updatePosition(finalPort);
						 //System.out.println("-- RouterCoordinator "+rh.getName()+"  getsecond true service notify");
						
						request.getFirst().service.notify(); // notify product thread that it is finished
//						try {
//							request.getFirst().service.wait();
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} // wait for notification of good reception to continue with another request	
						 //System.out.println("-- RouterCoordinator "+rh.getName()+"  notifie");

					}else{
						((ReactiveRouter_ROH_Behavior)(rh.getRoh().getBehavior())).requestResult= false;
						((ReactiveRouter_ROH_Behavior)(rh.getRoh().getBehavior())).requestResult= request.getSecond();
						//System.out.println("-- RouterCoordinator "+rh.getName()+"  getsecond else service notify");
						try {
							//Thread.sleep(10);
							request.getFirst().service.notify(); // notify product thread that it is finished
							request.getFirst().service.wait();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						// wait for notification of good reception to continue with another request
						 //System.out.println("-- RouterCoordinator "+rh.getName()+"  notifie");
					}
					//request.getFirst().service.notifyAll();
					//System.out.println("-- RouterCoordinator "+rh.getName()+"  end synchronized");

				}
				 
				//System.out.println("-- RouterCoordinator "+rh.getName()+"  Requestbox size="+requestInbox.size());
			}
			//System.out.println("-- RouterCoordinator "+rh.getName()+"  Requestbox empty");// go back to select another request
		} // go back to wait for arrival of new requests
				
	}			
	public boolean isWaiting() {
			return waiting;
		}
		//PRIVATE METHODS-------------------------------
	private  Pair<Task_RH, Boolean>  selectRequest() {
			ROH roh= this.rh.getRoh();

			HashSet<ResourceHolon> portOwners= null;
			//Look for a request matching the current Setup
			Iterator<Task_RH> it = requestInbox.iterator();
			 while(it.hasNext()){
				 Task_RH task= it.next();
				 
				 //Check task requiered setup
				 if(task.setup.equals(String.valueOf(roh.getCurrentSetup()))){  
					//ASK FOR PERMIT OF PORT TO DESTINATION
						String finalPort=task.service.getParameterByName("FinalPort").getValue();
						portOwners = Application.df.getPortOwners(finalPort);
						 for (ResourceHolon owner : portOwners) {
								if(!owner.getRoh().requestPortPermition(task.client,finalPort,0)){
									it.remove(); // remove request from inbox
									return (new Pair<Task_RH, Boolean>(task, false)); // return this request to be rejected
									}
							}
						 //ALL OWNERS AGREED PERMITION and SAME SET UP
						 it.remove(); // remove from inbox
						 return (new Pair<Task_RH, Boolean>(task, true)); // Authorize execution of this service
					}
			 }
			 //NO REQUEST WITH SAME SET UP
			 it = requestInbox.iterator();
			 while(it.hasNext()){
				 Task_RH task= it.next();
				//ASK FOR PERMIT OF PORT TO DESTINATION
					String finalPort=task.service.getParameterByName("FinalPort").getValue();
					portOwners = Application.df.getPortOwners(finalPort);
					 for (ResourceHolon owner : portOwners) {
							if(!owner.getRoh().requestPortPermition(task.client,finalPort,0)){
								it.remove(); // remove request from inbox
								return (new Pair<Task_RH, Boolean>(task, false)); // return this request to be rejected
								}
						}
					 //ALL OWNERS AGREED PERMITION and SAME SET UP
					 it.remove(); // remove request from inbox
					 return (new Pair<Task_RH, Boolean>(task, true)); // Authorize execution of this service
				}
			 return null;
		}
				 
				
	
}
