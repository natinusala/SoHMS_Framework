import Crosscutting.PathState;
import MService.MServiceSpecification;
import OrdersManagement.ROH;
import OrdersManagement.ROH_Behavior;
import ProductManagement.ProductHolon;
import ResourceManagement.ResourceHolon;

/**
 * This behavior is intended to Conveyers with queuing capacity. 
 * They do not need to command the execution of transport.(They are sumissive and passive)
 * Capacity of several units at the time.
 * Queue characteristic
 * One input port
 * One output port
 * No setups
 * @author gamboa-f
 *
 */
public class ReactiveBuffer_ROH_Behavior extends ROH_Behavior {
	
	//ATTRIBUTS-------------------------------
	public ResourceHolon linkedOutputRH;		// resource to the one its output is linked and on which its liberation depends
	
	//CONSTRUCTORS-------------------------------
	
	public ReactiveBuffer_ROH_Behavior() {}
	
	//PUBLIC METHODS-------------------------------

	@Override
	public  MServiceSpecification requestServiceExe(ProductHolon client, PathState prodTask, ROH roh) {
		
		//This is a submisive resource so execution is imminent once it accepted the port permission
	
		//Accept and Send Command
		// The operation was already reserved by the port request function
		
			// Change status of Pallet to In Transition
			String startport =prodTask.service.getParameterByName("StartPort").getValue(); 
			String finalPort =prodTask.service.getParameterByName("FinalPort").getValue();
		//	client.getAssociatedResource().declareInTransition(startport, finalPort);
		
			if (rh.getRoh().getAssociatedRH() instanceof BufferedRH){
				//System.out.println("== ROH_Behavior Transfert par d�faut demand� palette "+transporter._RFID+" � ressource "+rh.getName()+", consid�r� BufferedRH");
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
				
				//System.out.println("== ROH_Behavior Transfert par d�faut demand� palette "+transporter._RFID+" � ressource "+rh.getName()+", consid�r� RH normal");
				while(!(rh.getRoh().getNumOfCurrentExecutions()<=0)){
				try {
					//System.out.println("== ROH_Behavior Transfert par d�faut demand� palette "+transporter._RFID+" � ressource "+rh.getName()+", numOfCurrentExecutions+"+rh.roh.numOfCurrentExecutions);						
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				rh.getRoh().setNumOfCurrentExecutions(0);

			}
			
		System.out.println("++  ReactiveBuffer_ROH_Behavior "+rh.getName()+" SendServiceToField ="+prodTask.service);
		@SuppressWarnings("unused")
		boolean succesfullExec= rh.getSil().sendServiceToField(prodTask.service, client, 1);  // returns when execution has been completed
		rh.getRoh().setNumOfCurrentExecutions(rh.getRoh().getNumOfCurrentExecutions()-1);//Liberate a place 
		//System.out.println("++ "+client.associatedPallet._RFID + " ReactiveBuffer_ROH_Behavior2 "+rh.getName()+" rh.roh.numOfCurrentExecutions="+rh.roh.numOfCurrentExecutions);
		
			// Update new position of the Pallet ( confirmed by the SIL)
			//client.associatedPallet.upDatePosition(finalPort); 
		
		return prodTask.service;
		
	}
	//-------------------------------------------------------------------------------------------
	/**
	 *  Pure Reactive
	 *  If there is capacity now,  grant port.
	 *  If not deny
	 */
	
	@Override
	public boolean requestPortPermition(ProductHolon client, String finalPort, long timeFromNow, ROH roh) {
		synchronized (roh.getNumOfCurrentExecutions()) {
			BufferedRH rh = (BufferedRH)(roh.getAssociatedRH());
			//check capacity
			if (roh.getNumOfCurrentExecutions() < rh.getCapacity()) {
				roh.setNumOfCurrentExecutions(roh.getNumOfCurrentExecutions()+1);				 //Reserve occupation
				//System.out.println("++ "+client.associatedPallet._RFID + " ReactiveBuffer_ROH_Behavior "+rh.getName()+" rh.roh.numOfCurrentExecutions="+rh.roh.numOfCurrentExecutions);
				return true;
			} 
			else return false;
			
		}
	}
}
		
		
		
