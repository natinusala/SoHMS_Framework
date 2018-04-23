package MService;

import java.util.ArrayList;

public class MServiceProfile {

	//Attributes
	private MService MService;
	private ArrayList<ParametersProfile> parametersProfile;
	
	//Constructor
	public MServiceProfile() {}
	public MServiceProfile(MService service) {
		this.MService = service;
	}
	
	//Getters
	public MService getmService() {
		return MService;
	}
	public ArrayList<ParametersProfile> getParameterProfile() {
		return parametersProfile;
	}
	
	//Setters
	public void setmService(MService mService) {
		this.MService = mService;
	}
	public void setParametersProfile(ArrayList<ParametersProfile> parametersProfile) {
		this.parametersProfile = parametersProfile;
	}
	
	//Methods
	
	
}
