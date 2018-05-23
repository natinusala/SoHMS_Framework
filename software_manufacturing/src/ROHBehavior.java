import java.security.Policy.Parameters;

import Crosscutting.PathState;
import MService.MServiceSpecification;
import OrdersManagement.ROH;
import OrdersManagement.ROH_Behavior;
import ProductManagement.ProductHolon;
import ResourceManagement.Transporter;


public class ROHBehavior extends ROH_Behavior{

	@Override
	public MServiceSpecification requestServiceExe(ProductHolon client, PathState prodTask, ROH roh,String s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean requestPortPermition(ProductHolon client, String finalPort, long timeFromNow, ROH roh) {
		// TODO Auto-generated method stub
		return false;
	}
	public String requestDefaultTransfer(Transporter transporter, String port) {	
		
		// Change status of Pallet to In Transition
		transporter.declareInTransition(port, rh.getSil().getDefaultDestination());
		//System.out.println("== ROH_Behavior Transfert par d�faut demand� palette "+transporter._RFID+" � ressource "+rh.getName()+", SIL "+rh.sil.toString());
		if (rh.getRoh().getAssociatedRH() instanceof BufferedRH){
			//System.out.println("== ROH_Behavior Transfert par d�faut demand� palette "+transporter._RFID+" � ressource "+rh.getName()+", consid�r� BufferedRH");
		BufferedRH rh2 = (BufferedRH)(rh.getRoh().getAssociatedRH());
		//check capacity
			while(!(rh.getRoh().getNumOfCurrentExecutions()<rh2.getCapacity())){
			try {
				System.out.println("== ROH_Behavior Transfert par d�faut demand� palette "+transporter._RFID+" � ressource "+rh.getName()+", numOfCurrentExecutions+"+rh.getRoh().getNumOfCurrentExecutions());						
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
			//System.out.println("++ ROH_Behavior "+rh.getName()+" rh.roh.numOfCurrentExecutions="+rh.roh.numOfCurrentExecutions);

		}
		MServiceSpecification transportService= null; //rh.getSil().defaultAction(transporter,port); 
			
		//System.out.println("== ROH_Behavior Transfert par d�faut palette "+transporter._RFID+" re�u, service: "+transportService.toString());
		
if(transportService!= null){
	
	
}else{
	}
return port;
}
}
