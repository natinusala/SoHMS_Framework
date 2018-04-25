package ResourceManagement;

import java.util.ArrayList;

import MService.MServiceProfile;


public class RH_Profile extends Resource {
	
	//Attributs
	private ArrayList<MServiceProfile> offeredServices;
	
	//Constructors
	public RH_Profile() {}
	
    public RH_Profile (int resourceId, String name, String technology, String category, String textDescription,
			String[] inputPorts, String[] outputPorts) {
		 super(resourceId, name, technology, category, textDescription,
					 inputPorts,  outputPorts);
	}
   
    //Getters
    public ArrayList<MServiceProfile> getOfferedServices() {
		return offeredServices;
	}

    //Setters
    public void setOfferedServices(ArrayList<MServiceProfile> offeredServices) {
		this.offeredServices = offeredServices;
	}

}