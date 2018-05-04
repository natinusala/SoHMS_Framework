package MService;

import java.util.ArrayList;
import java.util.Arrays;



public class MServiceSpecification{
	
	//ATTRIBUTS-------------------------------
	private static int counter=0;
	private static int maxInst=1000;
	
	private int id;
	private MService mService;
    private ArrayList<Parameter> parameters;
	private ArrayList<ParametersProfile> attributeProfiles;
	
	
//CONSTRUCTORS-------------------------------
	public MServiceSpecification(){
		this.id= (counter % maxInst) +1; // rotational queue
		counter= this.id;
	}
	
	
//PUBLIC METHODS-------------------------------
	
	public String toString() {
		String str = "Service Name: "+mService.getName()+" ServiceID: " + id +"\nParameters:";
		
		for (int i = 0; i < parameters.size(); i++) {
			str= str.concat(parameters.get(i).toString());
		}
		 return str;
	}
	//----------------------------------------------------
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mService == null) ? 0 : mService.hashCode());
		//result = prime * result + Arrays.hashCode(parameters);
		return result;
	}
	//-----------------------------------------
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MServiceSpecification other = (MServiceSpecification) obj;
		if (mService == null) {
			if (other.mService != null)
				return false;
		} else if (!mService.equals(other.mService))
			return false;
		return true;
	}

	
	//SETTERS -----------------------------------
	public void setId(int id) {
		this.id = id;
	}
	
	public static void setCounter(int counter) {
		MServiceSpecification.counter = counter;
	}
	public static void setMaxInst(int maxInst) {
		MServiceSpecification.maxInst = maxInst;
	}
	public void setMService(MService mServType) {
		this.mService = mServType;
	}
	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}
	public void setAttributeProfiles(ArrayList<ParametersProfile> attributeProfiles) {
		this.attributeProfiles = attributeProfiles;
	}
	
	//GETTERS -----------------------------------
	public int getId() {
		return id;
	}
	public static int getCounter() {
		return counter;
	}
	public static int getMaxInst() {
		return maxInst;
	}
	public MService getMServiceType() {
		return mService;
	}
	public ArrayList<Parameter> getParameters() {
		return parameters;
	}
	public ArrayList<ParametersProfile> getAttributeProfiles() {
		return attributeProfiles;
	}
	public Parameter getParameterbyType(Parameter otherParam){ //TODO getParameter object
		for (int i = 0; i < parameters.size(); i++) {
			Parameter p= parameters.get(i); // Cast  to use equals @ Parameter class
			if(p.isSameParamType(otherParam)){ 
				return parameters.get(i);
			}
		}
		return null;	
	}
	 public Parameter getParameterByName(String name) {
			
			for (int i = 0; i < parameters.size(); i++) {
				Parameter p= parameters.get(i); // Cast  to use equals @ Parameter class
				if(p.getName().equalsIgnoreCase(name)){ 
					return parameters.get(i);
				}
			}		
			return null;
		}
	
		
}

