package OrdersManagement;

import java.util.Date;

import Crosscutting.PathState;
import MService.MServiceImplentation;
import ProductManagement.ProductHolon;
import ResourceManagement.ResourceHolon;
import ResourceManagement.Task_RH;




public abstract class ROH_Behavior{

	//Attributes
	private ResourceHolon rh;


	//Getters and Setters
	public ResourceHolon getRh() {
		return rh;
	}
	public void setRh(ResourceHolon rh) {
		this.rh = rh;
	}
	
	//methods
    public abstract MServiceImplentation requestServiceExe(ProductHolon client,PathState prodTask, ROH roh);
	public abstract boolean requestPortPermit(ProductHolon client,String finalPort, long timeFromNow, ROH roh);
	/**
	 * returns
	 *  the task TIME if yes in milliseconds
	 *  -1 if no time to execute but can negotiate
	 *  -2 if no time to negotiate;
	 * @param task
	 * @param associatedRH
	 * @return
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
	 public long getProcessTime(PathState prodTask){
       return 0;		 
	 }
	 public String requestDefaultTransfer() {return null;}
	 
}