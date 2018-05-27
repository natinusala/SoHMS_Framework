package OrdersManagement;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import Crosscutting.AutomatIterator;
import Crosscutting.PathState;
import Crosscutting.Transition;
import MService.MServiceSpecification;
import ProductManagement.ProductHolon;
import ResourceManagement.Transporter;
import Workshop.LayoutMap;

/**
 * Class with the function run that executes Production plans of a PH
 * @author Francisco
 *
 */
public class Reactive_POH_Behavior implements POH_Behavior, Runnable{

//ATTRIBUTS-------------------------------
	public POH associatedPOH;
	public String planState; // it is the label of the Automaton State that points the edges of that state

	//CONSTRUCTORS-------------------------------
	 public Reactive_POH_Behavior() {
	 }
	 
//PUBLIC METHODS-------------------------------
	@Override
	public void run() {
		
		ProductHolon ph=associatedPOH.associatedPH;
		Transporter pallet = ph.getAssociatedResource();
		AutomatIterator planIterator;

		FileWriter out = null;
		
		try {
			
			PrintWriter writer = new PrintWriter("data/INDIN2016POHlog"+ph.getId()+".txt", "UTF-8");
			writer.println("POH " +ph.getId() + " log:");
			writer.close();

			out = new FileWriter("data/INDIN2016POHlog"+ph.getId()+".txt", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		    
		    //exception handling left as an exercise for the reader
		
		//START EXECUTION OF PRODUCTION RECIPIE
		 while (!ph.getRecipe().isTerminated()){
		
			// Create Production Plan for the Actual position and Production State
			ph.getExploreBehavior().setNewPlan(pallet.actualPort, ph.getRecipe().getStateID(), null, null);
			planIterator= ph.getProductionPlan().automatIterator(pallet.actualPort);  // reset the iterator
			
			executeStep(ph, planIterator);
			//log 1 poh phID pallet timestamp
			java.util.Date date= new java.util.Date();
			try {
				out.write("1; POH;" + ph.getId()+";"+pallet._RFID +";" + new Timestamp(date.getTime()) +"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} // end while ( recipe)
		 try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		//Dispatch the Product
		ph.getExploreBehavior().setDispatchPlan(pallet.actualPort, ph.getRecipe().getStateID(), null, null);
		planIterator= ph.getProductionPlan().automatIterator(pallet.actualPort);  // reset the iterator		
		
		executeStep(ph, planIterator);
				
		// Notify Conclusion of Product
		System.out.println("Product Terminated");
		ph.productTerminated();


	}
//PRIVATE METHODS-------------------------------
	private void executeStep(ProductHolon ph, AutomatIterator planIterator) {
		
		@SuppressWarnings("unused")
		LayoutMap productionPlan= ph.getProductionPlan();
		Transporter pallet = ph.getAssociatedResource();
		ArrayList<Transition> alternatives;
		
		while(true){
			
			try {
				alternatives = planIterator.getNextTransitions();
			
				// Request Execution of Service belonging to the most promising plan
				int i=-1;
				MServiceSpecification result=null;
				PathState prodTask= null;
			
				//Loop iterating among present alternatives
				do{
					i++;
					if(i>alternatives.size()-1){
						i=0; 					// repeat requests
						Thread.sleep(500);  	// put a delay between set of requests
						//Integer taskID = Integer.parseInt(alternatives.get(i).label); // get the reference of the Task
						//prodTask=ph.planActions.get(taskID); //Get the Task
						//prodTask.provider.roh.behavior.requestDefaultTransfer(pallet, planIterator.getState());
						//break;
					}
					Integer taskID = Integer.parseInt(alternatives.get(i).label); // get the reference of the Task
					prodTask=ph.getActionsPlan().get(taskID); //Get the Task
					result=prodTask.provider.getRoh().requestServiceExe(ph,prodTask, null);//returns the service that was executed or null if rejected
					
					
					
					
					
				}while(result==null); // try all alternatives until success to request a service
				//System.out.println("** "+pallet._RFID + " Reactive_POH_Behavior SUCCESS result=" +result);
				
				// ANALYSE RESULT OF SERVICE

					
				// If another service was executed
				if(!result.equals(prodTask.service)){ //TODO: Must validate this situation
					
					//Update pallet position
					pallet.lastPort= result.getParameters().get(1).getValue();
					pallet.actualPort= result.getParameters().get(2).getValue();
					
					//Re-plan according to new position (supposition: all default services are a transport service)
					ph.getExploreBehavior().setNewPlan(pallet.actualPort, ph.getRecipe().getStateID(), null, null);
					planIterator= ph.getProductionPlan().automatIterator(pallet.actualPort);      // reset the iterator
				}
				
				// If successful
				else{
					try { // If it was a transport service
						if(result.getMServiceType().getCategory().equalsIgnoreCase("Transport")){
							planIterator.executeTransition(String.valueOf(prodTask.id)); // evolve according to destination of service
							
					 		 //System.out.println("** "+"Reactive_POH_Behavior Transport "+pallet);

							//pallet.upDatePosition(prodTask.service.getParameters()[1].getValue());
							//pallet.actualPort=prodTask.service.getParameters()[1].getValue();
							this.planState= planIterator.getStateID(); // update the planState of the POH
				
						}else { // If it was an MService
							
							// The plan was completed successfully for this step, re-plan and restart process
							planIterator.executeTransition(String.valueOf(prodTask.id)); // evolve according to destination of service
							this.planState= planIterator.getStateID(); // update the planState of the POH
							if(!ph.getRecipe().isTerminated()) this.associatedPOH.associatedPH.getRecipe().evolve(result); // Advance the recipe iterator
					 		 //System.out.println("** "+"Reactive_POH_Behavior NotTransport "+pallet);
					 		 pallet.upDatePosition(prodTask.outputPort);
							//pallet.actualPort=prodTask.outputPort;
							break;// finishes and goes back
						}
					
				} catch (Exception e) {
					System.out.println("AutomatonNotInitializedException: "+e.getMessage());
				}
				
			 } // End else
			} catch (Exception e) {
				System.out.println("Reactive POH Behavior Exception:"+e.getMessage());
			}
		}
}
//GETTERS & SETTERS -----------------------------------
	
	public POH getAssociatedPOH() {
		return associatedPOH;
	}
	public void setAssociatedPOH(POH associatedPOH) {
		this.associatedPOH = associatedPOH;
	}

	@Override
	public void setPlanState(String actualPort) {
		this.planState= actualPort;
	}

	public String getPlanState() {
		return planState;
	}
}
