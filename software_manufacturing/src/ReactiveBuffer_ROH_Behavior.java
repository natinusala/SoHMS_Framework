import Crosscutting.PathState;
import DirectoryFacilitator.DirectoryFacilitator;
import MService.MServiceSpecification;
import OrdersManagement.ROH;
import OrdersManagement.ROH_Behavior;
import ProductManagement.ProductHolon;
import ResourceManagement.ResourceHolon;
import ResourceManagement.Transporter;

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

	@Override
	public MServiceSpecification requestServiceExe(ProductHolon client, PathState prodTask, ROH roh,
			DirectoryFacilitator df) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean requestPortPermition(ProductHolon client, String finalPort, long timeFromNow, ROH roh) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String requestDefaultTransfer(Transporter transporter, String port) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
		
		
		
