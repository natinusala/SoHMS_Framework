package OrdersManagement;

import java.util.Date;

import MService.MServiceImplentation;
import ProductManagement.ProductHolon;
import ResourceManagement.ResourceHolon;



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
	 protected long haveTimeToDoIT(PathState prodTask, ROH roh) {return 0;}
	 public long getProcessTime(PathState prodTask){return 0;}
	 public String requestDefaultTransfer() {return null;}
	 
}