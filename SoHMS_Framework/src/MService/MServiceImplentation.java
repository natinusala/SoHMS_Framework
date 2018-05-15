package MService;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashSet;


public class MServiceImplentation{
	//Attributes
	private MService mService;
	private ArrayList<ParametersProfile> parametersProfile;
	private HashSet<ProcessMethod> processMethods;
	private ArrayList<String> inputs;
	private ArrayList<String> outputs;
	private Double averageCost;
	
	//Constructors
	public MServiceImplentation() {}
	
	public MServiceImplentation(MService mService, ArrayList<ParametersProfile> parametersProfile,
		HashSet<ProcessMethod> processMethods, ArrayList<String> inputs, ArrayList<String> outputs, Double averageCost) {
		this.mService = mService;
		this.parametersProfile = parametersProfile;
		this.processMethods = processMethods;
		this.inputs = inputs;
		this.outputs = outputs;
		this.averageCost = averageCost;
	}

	//Getters and Seterrs
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
	public ArrayList<String> getInputs() {
		return inputs;
	}
	public void setInputs(ArrayList<String> inputs) {
		this.inputs = inputs;
	}
	public ArrayList<String> getOutputs() {
		return outputs;
	}
	public void setOutputs(ArrayList<String> outputs) {
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
	
	public ProcessMethod getProcessMethod(Integer methodID) {
		for(ProcessMethod method : processMethods) {
			if(method.getId()==methodID) return method;
		}
		return null;
	}
	
	public ArrayList<Integer> getMatchingMethods(MServiceSpecification service) {
		// Get the parameter profile set that fits with the service
		if(parametersProfile!=null){
			ParametersProfile paramSet = getMatchingParamProfile(service);
			// get the methods of the parameter profile
			return paramSet.getAssociatedMethods();
		}else {
			 return getProcessMethodsIDs();
		}			
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
	
	public ParametersProfile getMatchingParamProfile(MServiceSpecification service) {
		for (ParametersProfile ppSet : parametersProfile) {
			boolean match= true;
			//PARAMETER check 						
			for(ProfileParameter pp: ppSet.getParamProfiles()){
				//Match Parameters
				SParameter paramInst= service.getParameterbyType(pp); // get the parameter specification object that fits in name and data type
				if(!pp.matchParam(paramInst)){ // match parameter instance and parameter profile
					match= false; 
					break;
				}
			}
			//All parameters where evaluated as a match so there is a set that matches	
			if(match== true)
				return ppSet;
		}
		return null; // no set returned a match
	}
	
    public boolean matchService(MServiceSpecification MSSpec) {
    	//Compare the description of the Services
		 MService msType = MSSpec.getMServiceType();
		 if(!this.mService.equals(msType)){
			 return false;
		 }
		 //Compare if Parameter Values fit in a Parameter Profile Set 
		 	if(parametersProfile== null) return true;
		     // CHECK SET
				for (ParametersProfile ppSet : parametersProfile) {
			 		boolean match= true;
			 		//PARAMETER check 						
			 		for(ProfileParameter pp: ppSet.getParamProfiles()){
			 			//Match Parameters
			 			SParameter paramInst= MSSpec.getParameterbyType(pp); // get the parameter specification object that fits in name and data type
			 			if(!pp.matchParam(paramInst)){ // match parameter instance and parameter profile
			 				match= false; 
			 				break;
			 			}
			 		}
			 	//All parameters where evaluated as a match so there is a set that matches	
				 if(match== true)
					 return true;
			 }
			 return false; // no set returned a match
    }
    
    public void addParameterdProfile(ParametersProfile pp){
		if(this.parametersProfile==null){
			this.parametersProfile= new ArrayList<ParametersProfile>();
		}
		this.parametersProfile.add(pp);
	}
    
 
}
