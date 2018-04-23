package MService;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashSet;



public class MServiceImplentation{
	//Attributes
	private MService mService;
	private ArrayList<ParametersProfile> parametersProfile;
	private HashSet<ProcessMethod> processMethods;
	private List inputs;
	private List outputs;
	private Double averageCost;
	
	//Constructors
	public MServiceImplentation() {}
	
	public MServiceImplentation(MService mService, ArrayList<ParametersProfile> parametersProfile,
		HashSet<ProcessMethod> processMethods, List inputs, List outputs, Double averageCost) {
		this.mService = mService;
		this.parametersProfile = parametersProfile;
		this.processMethods = processMethods;
		this.inputs = inputs;
		this.outputs = outputs;
		this.averageCost = averageCost;
	}

	//Getters ans Seterrs
	public MService getmService() {
		return mService;
	}
	public void setmServType(MService mService) {
		this.mService = mService;
	}
	public ArrayList<ParametersProfile> getParametersProfile() {
		return parametersProfile;
	}
	public void setParametersProfile(ArrayList<ParametersProfile> parametersProfile) {
		this.parametersProfile = parametersProfile;
	}
	public HashSet<ProcessMethod> getProcessMethods() {
		return processMethods;
	}
	public void setProcessMethods(HashSet<ProcessMethod> processMethods) {
		this.processMethods = processMethods;
	}
	public List getInputs() {
		return inputs;
	}
	public void setInputs(List inputs) {
		this.inputs = inputs;
	}
	public List getOutputs() {
		return outputs;
	}
	public void setOutputs(List outputs) {
		this.outputs = outputs;
	}
	public Double getAverageCost() {
		return averageCost;
	}
	public void setAverageCost(Double averageCost) {
		this.averageCost = averageCost;
	}
	
	//Methods
	public MServiceProfile generateMServiceProfile() {
		MServiceProfile pofile = new MServiceProfile();
		pofile.setmService(this.mService);
		pofile.setParametersProfile(this.parametersProfile);
		return pofile;
	}
	
	public ProcessMethod getProcessMethodByMethodID(Integer methodID) {
		for(ProcessMethod method : processMethods) {
			if(method.getId()==methodID) return method;
		}
		return null;
	}
	
	public void getMatchingMethodsOfMService() {
	}
	
	public ArrayList<Integer> getProcessMethodsIDs() {
		
		ArrayList<Integer> methodsIDs = new  ArrayList<Integer>();
		int i=0;
	    for (ProcessMethod method : this.processMethods) {
		  methodsIDs.add(i, method.getId());
		  i++;
	    }
	  return methodsIDs;
	}
	
	public void getMatchingParamProfile() {}
	
    public boolean matchService() {
    	return false;
    }
    
    public void addParameterdProfile(ParametersProfile pp){
		if(this.parametersProfile==null){
			this.parametersProfile= new ArrayList<ParametersProfile>();
		}
		this.parametersProfile.add(pp);
	}
}
