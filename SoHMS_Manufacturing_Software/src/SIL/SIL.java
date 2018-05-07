package SIL;

import java.util.HashSet;

import ResourceManagement.RH_SIL;

public class SIL {

	//Attributes
	private HashSet<RH_SIL> ressource_SILs;
	//Constructors
	public SIL(){
		this.ressource_SILs = new HashSet<RH_SIL>();
	}
	//Getters & Setters
	public HashSet<RH_SIL> getRessource_SILs() {
		return ressource_SILs;
	}

	public void setRessource_SILs(HashSet<RH_SIL> ressource_SILs) {
		this.ressource_SILs = ressource_SILs;
	}

	//Methods
	public RH_SIL getRHSILByClass(Class rhSILClass) {
		for (RH_SIL rh_SIL : ressource_SILs) {
			if(rh_SIL.getClass()== rhSILClass){
				return rh_SIL;
			}
		}
		return null;
	}
	
	public void associateSIL2Ressource(RH_SIL r) {
    	this.ressource_SILs.add(r);
    }
}
