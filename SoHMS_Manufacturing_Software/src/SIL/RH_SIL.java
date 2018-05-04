package SIL;

import Crosscutting.Pair;
import MService.MServiceImplentation;
import MService.MServiceSpecification;
import OrdersManagement.ROH;
import ProductManagement.ProductHolon;

public abstract class RH_SIL {
	public abstract boolean sendServiceToField(MServiceSpecification service, ProductHolon client,int method);
	public abstract Pair<Integer, Long> getFastestMethod(MServiceImplentation serviceImp);
	public abstract long getMethodTime(Integer methodID);
	public abstract long getSetupTime(int currentSetup, int setup);
	public abstract void changeSetup(int setup);
	public abstract void oK();
	public abstract void start();
	public abstract void end();
	public abstract ROH getROH();
	public abstract String getDefaultDestination();
}
