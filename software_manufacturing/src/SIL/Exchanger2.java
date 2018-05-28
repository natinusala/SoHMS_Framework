package SIL;

import Crosscutting.*;
import MService.*;
import OrdersManagement.*;
import ProductManagement.*;
import ResourceManagement.*;

public class Exchanger2 implements RH_SIL{

@Override
public boolean sendServiceToField(MServiceSpecification service, ProductHolon client, int method) {
	// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Pair<Integer, Long> getFastestMethod(MServiceImplentation serviceImp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getMethodTime(Integer methodID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getSetupTime(int currentSetup, int setup) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MServiceSpecification defaultAction(Transporter transporter, String inputPort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeSetup(int setup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void oK() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ROH getROH() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultDestination() {
		// TODO Auto-generated method stub
		return null;
	}
}