import Crosscutting.PathState;
import MService.MServiceImplentation;
import MService.MServiceSpecification;
import MService.ProcessMethod;
import OrdersManagement.ROH;
import OrdersManagement.ROH_Behavior;
import ProductManagement.ProductHolon;
import ResourceManagement.ResourceHolon;

/**
 * This behavior represents a router  exchanging product among input and output ports.
 * It has several inputs and several outputs and only one mechanism to route one product at the time.
 * 
 * @author gamboa-f
 *
 */
public class ReactiveRouter_ROH_Behavior extends ROH_Behavior{
	
	//ATTRIBUTS--------------------------------------
	public RouterCoordinator coordinator;
	public boolean requestResult;
	
	//CONSTRUCTORS--------------------------------
	 //-----------------------------------------------
	public ReactiveRouter_ROH_Behavior(ResourceHolon rh) {
		this.rh= rh;
		this.coordinator = new RouterCoordinator(rh);
		coordinator.start();
		this.requestResult=false; //rh.getRoh().setNumOfCurrentExecutions()
	}
	//------------------------------------------
	

	// PUBLIC METHODS------------------------------------------------------------
	@Override
	public MServiceSpecification requestServiceExe(ProductHolon client, PathState prodTask, ROH roh) {
		
		//Add the request to the inbox
		synchronized (coordinator.requestInbox) {
			MServiceImplentation servImp = roh.getAssociatedRH().getServByType(prodTask.service.getMServiceType());
			 Integer[] methods=null; //servImp.getMatchingMethods(prodTask.service);
			 ProcessMethod method = servImp.getProcessMethod(methods[0]);
			 Task_RH task =new Task_RH(prodTask.service, client, String.valueOf(method.getSetup()),method.getId(),rh.getSil().getMethodTime(method.getId()), null , null);
				//System.out.println("++ "+client.associatedPallet._RFID + " ReactiveRouter_ROH_Behavior1 "+rh.getName()+" rh.roh.numOfCurrentExecutions="+rh.roh.numOfCurrentExecutions);
			 while(!(rh.getRoh().getNumOfCurrentExecutions()<1)){
					try {
						//System.out.println("== ROH_Behavior Transfert par d�faut demand� palette "+transporter._RFID+" � ressource "+rh.getName()+", numOfCurrentExecutions+"+rh.roh.numOfCurrentExecutions);						
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
			 //System.out.println("++ "+client.associatedPallet._RFID + " ReactiveRouter_ROH_Behavior task=" +task.toString());
				//System.out.println("++ "+client.associatedPallet._RFID + " ReactiveRouter_ROH_Behavior coordinator.requestInbox.size=" +coordinator.requestInbox.size());
				//System.out.println("++ "+client.associatedPallet._RFID + " ReactiveRouter_ROH_Behavior coordinator.waiting=" +coordinator.isWaiting());
				int taille=coordinator.requestInbox.size();
				coordinator.requestInbox.add(task);
				//System.out.println("++ "+client.associatedPallet._RFID + " ReactiveRouter_ROH_Behavior try to add task");
				
				while (taille==coordinator.requestInbox.size()){
					try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					rh.getRoh().setNumOfCurrentExecutions(rh.getRoh().getNumOfCurrentExecutions()+1);
				//System.out.println("++ "+client.associatedPallet._RFID + " ReactiveRouter_ROH_Behavior2 "+rh.getName()+" rh.roh.numOfCurrentExecutions="+rh.roh.numOfCurrentExecutions);
					
					coordinator.requestInbox.add(task);
					//System.out.println("++ "+client.associatedPallet._RFID + " ReactiveRouter_ROH_Behavior try to add task and waiting for coordinator 500ms");
					}
				
				
			//System.out.println("++ "+client.associatedPallet._RFID + " ReactiveRouter_ROH_Behavior coordinator.requestInbox.size=" +coordinator.requestInbox.size());
			coordinator.requestInbox.notify(); // notify presence of Request
		}
		//wait notification on its conclusion
		synchronized (prodTask.service) {
			try { 
				//System.out.println("++ "+client.associatedPallet._RFID + " ReactiveRouter_ROH_Behavior task waiting for service" );
				prodTask.service.wait(); // wait notification on this service to see if done or rejected according to the return value.
				//System.out.println("++ "+client.associatedPallet._RFID + " ReactiveRouter_ROH_Behavior task service done" );
				boolean r= requestResult;
				//System.out.println("++ "+client.associatedPallet._RFID + " ReactiveRouter_ROH_Behavior task service done result="+r );
				prodTask.service.notify(); // notify that the service have been responded properly and can continue to handle other requests
				//System.out.println("++ "+client.associatedPallet._RFID + " ReactiveRouter_ROH_Behavior task service notified" );
				if(r==true){
					return prodTask.service;
				}
				else {
					return null;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
//----------------------------------------------------------------
	/**
	 * Full reactive has no shedules and will always grant permision
	 */


	@Override
	public boolean requestPortPermition(ProductHolon client, String finalPort, long timeFromNow, ROH roh) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
