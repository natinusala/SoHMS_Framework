package ResourceManagement;

import Crosscutting.Pair;
import MService.MServiceImplentation;
import MService.MServiceSpecification;
import OrdersManagement.ROH;
import ProductManagement.ProductHolon;

public interface RH_SIL {
	
	public boolean sendServiceToField(MServiceSpecification service, ProductHolon client,int method);
	public Pair<Integer, Long> getFastestMethod(MServiceImplentation serviceImp);
	public long getMethodTime(Integer methodID);
	public long getSetupTime(int currentSetup, int setup);
	public MServiceSpecification defaultAction(Transporter transporter, String inputPort);
	public void changeSetup(int setup);
	public void oK();
	public void start();
	public void end();
	public ROH getROH();
	public String getDefaultDestination();
}