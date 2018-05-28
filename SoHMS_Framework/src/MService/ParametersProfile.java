package MService;

import java.util.ArrayList;

public class ParametersProfile {
	
	//Attributes
	private ArrayList<ProfileParameter> paramProfiles;
	private ArrayList<Integer> associatedMethods;
	
	//Construcors
	public ParametersProfile() {}
	public ParametersProfile(ArrayList<ProfileParameter> paramProfiles, ArrayList<Integer> associatedMethods) {
		this.paramProfiles= paramProfiles;
		this.associatedMethods = associatedMethods;
	}
	
	//Getters
	public ArrayList<ProfileParameter> getParamProfiles() {
		return paramProfiles;
	}
	public ArrayList<Integer> getAssociatedMethods() {
		return associatedMethods;
	}

	//Setters
	public void setParamProfiles(ArrayList<ProfileParameter> paramProfiles) {
		this.paramProfiles = paramProfiles;
	}
	public void setAssociatedMethods(ArrayList<Integer> associatedMethods) {
		this.associatedMethods = associatedMethods;
	}
	
	//methods
	public String DisplayParameterProfile() {
		String str = "\n ParametersProfile : ";
		for (int i = 0; i < paramProfiles.size(); i++) {
			str= str.concat(this.paramProfiles.get(i).toString().trim());
		}
		str= str.concat("\n The associated ethods: ");
		for (int i = 0; i < this.associatedMethods.size(); i++) {
			str= str.concat(associatedMethods.get(i).toString());
		}
		return str;
	}
}
