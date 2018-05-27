import Crosscutting.PathState;
import DirectoryFacilitator.DirectoryFacilitator;
import MService.MServiceImplentation;
import MService.MServiceSpecification;
import MService.ProcessMethod;
import OrdersManagement.ROH;
import OrdersManagement.ROH_Behavior;
import ProductManagement.ProductHolon;
import ResourceManagement.ResourceHolon;
import ResourceManagement.Task_RH;
import ResourceManagement.Transporter;

/**
 * This behavior represents a router  exchanging product among input and output ports.
 * It has several inputs and several outputs and only one mechanism to route one product at the time.
 * 
 * @author gamboa-f
 *
 */
public class ReactiveRouter_ROH_Behavior extends ROH_Behavior{

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
