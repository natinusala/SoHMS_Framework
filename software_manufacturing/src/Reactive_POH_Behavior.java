

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import Crosscutting.PathState;
import Crosscutting.Transition;
import MService.MServiceSpecification;
import OrdersManagement.POH;
import OrdersManagement.POH_Behavior;
import ProductManagement.ProductHolon;
import ResourceManagement.Resource;
import ResourceManagement.ResourceHolon;
import Workshop.LayoutMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;


/**
 * Class with the function run that executes Production plans of a PH
 * @author Francisco
 *
 */
public class Reactive_POH_Behavior implements POH_Behavior{


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
		ResourceHolon pallet = ph.getAssociatedResource();
		AutomatIterator planIterator;

		FileWriter out = null;
		
		try {
			
			PrintWriter writer = new PrintWriter("data/INDIN2016POHlog"+ph.getId()+".txt", "UTF-8");
			writer.println("POH " +ph.getId()+ " log:");
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
			//ph.getExploreBehavior().setNewPlan(pallet.actualPort, ph.recipe.getStateID());
			planIterator= null; //ph.productionPlan.automatIterator(pallet.actualPort);  // reset the iterator
			
			executeStep(ph, planIterator);
			//log 1 poh phID pallet timestamp
			java.util.Date date= new java.util.Date();
			try {
				//out.write("1; POH;" + ph.id+";"+pallet._RFID +";" + new Timestamp(date.getTime()) +"\n");
			} catch (Exception e) {
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
	//	ph.getExploreBehavior().setDispatchPlan(pallet.actualPort, ph.recipe.getStateID());
		planIterator= null ;//ph.getActionsPlan().automatIterator(pallet.);  // reset the iterator		
		
		executeStep(ph, planIterator);
				
		// Notify Conclusion of Product
		System.out.println("Product Terminated");
		ph.productTerminated();


	}
//PRIVATE METHODS-------------------------------
	private void executeStep(ProductHolon ph, AutomatIterator planIterator) {
		
		
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
